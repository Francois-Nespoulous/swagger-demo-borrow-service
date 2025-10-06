package com.example.borrow.domain.service;

import com.example.borrow.client.BookClient;
import com.example.borrow.client.UserClient;
import com.example.borrow.domain.enums.BorrowStatus;
import com.example.borrow.domain.model.BookDefinition;
import com.example.borrow.domain.model.BookInstance;
import com.example.borrow.domain.model.Borrow;
import com.example.borrow.domain.model.User;
import com.example.borrow.dto.in.BookDefinitionClientDto;
import com.example.borrow.dto.in.BookInstanceClientDto;
import com.example.borrow.dto.in.UserClientDto;
import com.example.borrow.exception.specific.*;
import com.example.borrow.mapper.BookDefinitionMapper;
import com.example.borrow.mapper.BookInstanceMapper;
import com.example.borrow.mapper.BorrowMapper;
import com.example.borrow.mapper.UserMapper;
import com.example.borrow.persistence.repository.entity.BorrowEntity;
import com.example.borrow.repository.BorrowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j //TODO pas lombok ! Logger log = ...
public class BorrowService {
    private static final int MAX_BORROWED_BOOKS_PER_USER = 3;

    private final BorrowRepository borrowRepository;
    private final UserClient userClient;
    private final BookClient bookClient;

    @Autowired
    public BorrowService(BorrowRepository borrowRepository,
                         UserClient userClient, BookClient bookClient) {
        this.borrowRepository = borrowRepository;
        this.userClient = userClient;
        this.bookClient = bookClient;
    }

    @Transactional
    public Borrow borrowBook(UUID bookInstanceId) {
        User user = this.loggedUser();

        return borrowBookByUser(bookInstanceId, user);
    }

    @Transactional
    public Borrow borrowBookAdmin(UUID bookInstanceId, UUID userId) {
        User user = this.userFromId(userId);

        return borrowBookByUser(bookInstanceId, user);
    }

    private Borrow borrowBookByUser(UUID bookInstanceId, User user) {
        this.assertBookInstanceNotAlreadyBorrowed(bookInstanceId);
        BookInstance bookInstance = this.bookInstanceFromId(bookInstanceId);

        this.assertUserCanBorrow(user);

        try {
//            bookInstance.updateLastAttempt();
//            bookInstanceRepository.save(BookInstanceMapper.toEntity(bookInstance)); TODO pour optimist lock

            Borrow borrow = new Borrow(null, bookInstance, user, BorrowStatus.ONGOING, LocalDateTime.now(), null);

            borrow = BorrowMapper.toDomain(
                    borrowRepository.save(BorrowMapper.toEntity(borrow)),
                    this.refreshBookInstance(bookInstance),
                    this.refreshUser(user));
            log.info("Book borrowed");
            log.debug("Borrow created: {}", borrow);
            return borrow;
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrentBorrowException(bookInstanceId);
        }
    }

    @Transactional
    public Borrow returnBook(UUID borrowId) {
        User user = this.loggedUser();

        return returnBookByUser(borrowId, user);
    }

    @Transactional
    public Borrow returnBookAdmin(UUID borrowId, UUID userId) {
        User user = this.userFromId(userId);

        return returnBookByUser(borrowId, user);
    }

    private Borrow returnBookByUser(UUID borrowId, User user) {
        Borrow borrow = this.borrowFromId(borrowId);

        this.assertBookInstanceNotAlreadyReturned(borrow);
        this.assertUserCanReturnBorrow(borrow, user);

        try {
            borrow.returnBookNow();
//            bookInstanceRepository.save(BookInstanceMapper.toEntity(borrow.getBookInstance())); TODO pour optimist lock

            borrow = BorrowMapper.toDomain(
                    borrowRepository.save(BorrowMapper.toEntity(borrow)),
                    this.refreshBookInstance(borrow.getBookInstance()),
                    this.refreshUser(borrow.getUser()));
            log.info("Book returned");
            log.debug("Borrow updated: {}", borrow);
            return borrow;
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrentReturnException(borrow.getBookInstance().getId());
        }
    }

    public List<Borrow> getBorrowHistory() {
        User user = this.loggedUser();

        return borrowRepository.getAllByUserId(user.getId())
                .stream()
                .map(entity -> BorrowMapper.toDomain(
                        entity,
                        this.enrichBookInstance(bookClient.getBookInstance(entity.getBookInstanceId())),
                        user))
                .toList();
    }

    private User loggedUser() {
        return this.enrichUser(userClient.getLoggedUser());
    }

    private User userFromId(UUID userId) {
        return this.enrichUser(userClient.getUser(userId));
    }

    private BookInstance bookInstanceFromId(UUID bookInstanceId) {
        return this.enrichBookInstance(bookClient.getBookInstance(bookInstanceId));
    }

    private Borrow borrowFromId(UUID borrowId) {
        BorrowEntity borrowEntity = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new BorrowNotFoundException(borrowId));

        BookInstance bookInstance = this.enrichBookInstance(bookClient.getBookInstance(borrowEntity.getBookInstanceId()));
        User user = this.enrichUser(userClient.getUser(borrowEntity.getUserId()));
        return BorrowMapper.toDomain(
                borrowEntity,
                bookInstance,
                user);
    }


    private void assertBookInstanceNotAlreadyBorrowed(UUID bookInstanceId) {
        if (borrowRepository.existsByBookInstanceId_AndStatusEquals(bookInstanceId, BorrowStatus.ONGOING))
            throw new AlreadyBorrowedException(bookInstanceId);
    }

    private void assertBookInstanceNotAlreadyReturned(Borrow borrow) {
        if (borrow.getStatus() != BorrowStatus.ONGOING)
            throw new AlreadyReturnedException(borrow.getId());
    }

    private void assertUserCanBorrow(User user) {
        if (borrowRepository.countBorrowedByUserId_AndStatusEquals(user.getId(), BorrowStatus.ONGOING) >= MAX_BORROWED_BOOKS_PER_USER) // TODO var env ?
            throw new TooMuchBooksBorrowedForUserException(user.getUsername());
    }

    private void assertUserCanReturnBorrow(Borrow borrow, User user) {
        if (!Objects.equals(borrow.getUser().getUsername(), user.getUsername()) && !user.getUserRole().canReturnAnyBooks())
            throw new UserNotAllowedToReturnBookException(user.getUsername());
    }


    private User enrichUser(UserClientDto userClientDto) {
        return UserMapper.toDomain(userClientDto, getNumberOfBooksBorrowedByUser(userClientDto.id()));
    }

    private User refreshUser(User user) {
        user.setNbOfBooksBorrowed(getNumberOfBooksBorrowedByUser(user.getId()));
        return user;
    }

    private BookInstance enrichBookInstance(BookInstanceClientDto bookInstanceClientDto) {
        BookDefinition bookDefinition = enrichBookDefinition(bookInstanceClientDto.bookDefinition());
        return BookInstanceMapper.toDomain(bookInstanceClientDto, bookDefinition, isBookBorrowed(bookInstanceClientDto.id()));
    }

    private BookInstance refreshBookInstance(BookInstance bookInstance) {
        BookDefinition bookDefinition = refreshBookDefinition(bookInstance.getBookDefinition());
        bookInstance.setBookDefinition(bookDefinition);
        bookInstance.setBorrowed(isBookBorrowed(bookInstance.getId()));
        return bookInstance;
    }

    //TODO maybe service a part ? qui juste enrichi
    private BookDefinition enrichBookDefinition(BookDefinitionClientDto bookDefinitionClientDto) {
        List<BookInstance> instances = bookDefinitionClientDto.bookInstances().stream()
                .map(bookInstanceClientDto -> {
                    boolean borrowed = isBookBorrowed(bookInstanceClientDto.id());
                    return BookInstanceMapper.toDomain(bookInstanceClientDto, null, borrowed);
                })
                .toList();

        int nbTotal = instances.size();
        int nbBorrowed = (int) instances.stream().filter(BookInstance::isBorrowed).count();

        BookDefinition bookDefinition = BookDefinitionMapper.toDomain(bookDefinitionClientDto, instances, nbTotal, nbBorrowed);

        instances.forEach(bookInstance -> bookInstance.setBookDefinition(bookDefinition));

        return bookDefinition;
    }

    private BookDefinition refreshBookDefinition(BookDefinition bookDefinition) {
        List<BookInstance> instances = bookDefinition.getBookInstances();
        instances.forEach(bookInstance -> bookInstance.setBorrowed(isBookBorrowed(bookInstance.getId())));

        bookDefinition.setNbTotalBooks(instances.size());
        bookDefinition.setNbBorrowedBooks((int) instances.stream().filter(BookInstance::isBorrowed).count());

        return bookDefinition;
    }


    public int getNumberOfBooksBorrowedByUser(UUID userId) {
        return borrowRepository.countBorrowedByUserId_AndStatusEquals(userId, BorrowStatus.ONGOING);
    }

    public boolean isBookBorrowed(UUID bookInstanceId) {
        return borrowRepository.existsByBookInstanceId_AndStatusEquals(bookInstanceId, BorrowStatus.ONGOING);
    }
}