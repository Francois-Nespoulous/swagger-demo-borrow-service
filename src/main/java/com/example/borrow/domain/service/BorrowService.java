package com.example.borrow.domain.service;

import com.example.borrow.client.UserClient;
import com.example.borrow.domain.enums.BorrowStatus;
import com.example.borrow.domain.model.BookInstance;
import com.example.borrow.domain.model.Borrow;
import com.example.borrow.domain.model.User;
import com.example.borrow.exception.specific.*;
import com.example.borrow.mapper.BookInstanceMapper;
import com.example.borrow.mapper.BorrowMapper;
import com.example.borrow.mapper.UserMapper;
import com.example.borrow.repository.BookInstanceRepository;
import com.example.borrow.repository.BorrowRepository;
import com.example.borrow.repository.UserRepository;
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

    private final BookInstanceRepository bookInstanceRepository ;
    private final UserRepository userRepository;
    private final BorrowRepository borrowRepository;
    private final UserClient userClient;

    @Autowired
    public BorrowService(BookInstanceRepository bookInstanceRepository, UserRepository userRepository, BorrowRepository borrowRepository,
                         UserClient userClient) {
        this.bookInstanceRepository = bookInstanceRepository;
        this.userRepository = userRepository;
        this.borrowRepository = borrowRepository;
        this.userClient = userClient;
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
            bookInstance.updateLastAttempt();
            bookInstanceRepository.save(BookInstanceMapper.toEntity(bookInstance));

            Borrow borrow = new Borrow(null, bookInstance, user, BorrowStatus.ONGOING, LocalDateTime.now(), null);

            return BorrowMapper.toDomain(
                    borrowRepository.save(BorrowMapper.toEntity(borrow)),
                    borrowRepository);
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
            bookInstanceRepository.save(BookInstanceMapper.toEntity(borrow.getBookInstance()));

            return BorrowMapper.toDomain(
                    borrowRepository.save(BorrowMapper.toEntity(borrow)),
                    borrowRepository);
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrentReturnException(borrow.getBookInstance().getId());
        }
    }

    public List<Borrow> getBorrowHistory() {
        User user = this.loggedUser();

        return borrowRepository.getAllByUser_Id(user.getId())
                .stream()
                .map(entity -> BorrowMapper.toDomain(entity, borrowRepository))
                .toList();
    }


    private User loggedUser() {
        return UserMapper.toDomain(userClient.getLoggedUser());
    }

    private User userFromId(UUID userId) {
        return UserMapper.toDomain(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId)),
                borrowRepository);
    }

    private BookInstance bookInstanceFromId(UUID bookInstanceId) {
        return BookInstanceMapper.toDomain(
                bookInstanceRepository.findById(bookInstanceId)
                        .orElseThrow(() -> new BookInstanceNotFoundException(bookInstanceId)),
                borrowRepository);
    }

    private Borrow borrowFromId(UUID borrowId) {
        return BorrowMapper.toDomain(
                borrowRepository.findById(borrowId)
                        .orElseThrow(() -> new BorrowNotFoundException(borrowId)),
                borrowRepository);
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
        if (borrowRepository.countBorrowedByUser_Id_AndStatusEquals(user.getId(), BorrowStatus.ONGOING) >= maxBorrowedBooksPerUser) // TODO var env ?
            throw new TooMuchBooksBorrowedForUserException(user.getUsername());
    }

    private void assertUserCanReturnBorrow(Borrow borrow, User user) {
        if (!Objects.equals(borrow.getUser().getUsername(), user.getUsername()) && !user.getUserRole().canReturnAnyBooks())
            throw new UserNotAllowedToReturnBookException(user.getUsername());
    }

    private void assertBorrowEntitiesExist(Borrow borrow) {
        if (!userRepository.existsById(borrow.getUser().getId()))
            throw new UserNotFoundException(borrow.getUser().getId());

        if (!bookInstanceRepository.existsById(borrow.getBookInstance().getId()))
            throw new BookInstanceNotFoundException(borrow.getBookInstance().getId());
    }
}