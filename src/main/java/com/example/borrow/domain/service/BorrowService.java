package com.example.borrow.domain.service;

import com.example.borrow.client.BookClient;
import com.example.borrow.client.UserClient;
import com.example.borrow.domain.enums.BorrowStatus;
import com.example.borrow.domain.model.BookInstance;
import com.example.borrow.domain.model.Borrow;
import com.example.borrow.domain.model.User;
import com.example.borrow.exception.specific.*;
import com.example.borrow.mapper.BookInstanceMapper;
import com.example.borrow.mapper.BorrowMapper;
import com.example.borrow.mapper.UserMapper;
import com.example.borrow.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class BorrowService {
    private static final int maxBorrowedBooksPerUser = 3;

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

            return BorrowMapper.toDomain( //TODO borrowRepository pas encore actu, donc client => old value
                    borrowRepository.save(BorrowMapper.toEntity(borrow)),
                    bookClient, userClient);
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

        this.assertBorrowEntitiesExist(borrow);
        this.assertBookInstanceNotAlreadyReturned(borrow);
        this.assertUserCanReturnBorrow(borrow, user);

        try {
            borrow.returnBookNow();
//            bookInstanceRepository.save(BookInstanceMapper.toEntity(borrow.getBookInstance())); TODO pour optimist lock

            return BorrowMapper.toDomain( //TODO borrowRepository pas encore actu, donc client => old value
                    borrowRepository.save(BorrowMapper.toEntity(borrow)),
                    bookClient, userClient);
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrentReturnException(borrow.getBookInstance().getId());
        }
    }

    public List<Borrow> getBorrowHistory() {
        User user = this.loggedUser();

        return borrowRepository.getAllByUserId(user.getId())
                .stream()
                .map(entity -> BorrowMapper.toDomain(entity, bookClient, userClient))
                .toList();
    }


    private User loggedUser() {
        return UserMapper.toDomain(userClient.getLoggedUser());
    }

    private User userFromId(UUID userId) {
        return UserMapper.toDomain(
                userClient.getUser(userId));
    }

    private BookInstance bookInstanceFromId(UUID bookInstanceId) {
        return BookInstanceMapper.toDomain(
                bookClient.getBookInstance(bookInstanceId));
    }

    private Borrow borrowFromId(UUID borrowId) {
        return BorrowMapper.toDomain(
                borrowRepository.findById(borrowId)
                        .orElseThrow(() -> new BorrowNotFoundException(borrowId)),
                bookClient, userClient);
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
        if (borrowRepository.countBorrowedByUserId_AndStatusEquals(user.getId(), BorrowStatus.ONGOING) >= maxBorrowedBooksPerUser) // TODO var env ?
            throw new TooMuchBooksBorrowedForUserException(user.getUsername());
    }

    private void assertUserCanReturnBorrow(Borrow borrow, User user) {
        if (!Objects.equals(borrow.getUser().getUsername(), user.getUsername()) && !user.getUserRole().canReturnAnyBooks())
            throw new UserNotAllowedToReturnBookException(user.getUsername());
    }

    private void assertBorrowEntitiesExist(Borrow borrow) { //TODO optional plutot ? ou on laisse les services gérer les erreurs ?
        if (userClient.getUser(borrow.getUser().getId()) == null)
            throw new UserNotFoundException(borrow.getUser().getId());

        if (bookClient.getBookInstance(borrow.getBookInstance().getId()) == null)
            throw new BookInstanceNotFoundException(borrow.getBookInstance().getId());
    }


    public int getNumberOfBooksBorrowedByUser(UUID userId) {
        return borrowRepository.countBorrowedByUserId_AndStatusEquals(userId, BorrowStatus.ONGOING);
    }

    public int getNumberOfBooksBorrowedAmongBookInstances(List<UUID> bookInstanceIds) {
        return (int) bookInstanceIds
                .stream()
                .filter(this::isBookBorrowed)
                .count();
    }

    public boolean isBookBorrowed(UUID bookInstanceId) {
        return borrowRepository.existsByBookInstanceId_AndStatusEquals(bookInstanceId, BorrowStatus.ONGOING);
    }
}