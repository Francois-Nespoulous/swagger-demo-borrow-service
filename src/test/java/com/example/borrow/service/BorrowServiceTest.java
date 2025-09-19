package com.example.borrow.service;

import com.example.borrow.domain.enums.BorrowStatus;
import com.example.borrow.domain.model.Borrow;
import com.example.borrow.domain.service.BorrowService;
import com.example.borrow.persistence.repository.entity.BorrowEntity;
import com.example.borrow.persistence.repository.entity.UserEntity;
import com.example.borrow.persistence.repository.entity.BookDefinitionEntity;
import com.example.borrow.persistence.repository.entity.BookInstanceEntity;
import com.example.borrow.domain.enums.BookState;
import com.example.borrow.repository.BookInstanceRepository;
import com.example.borrow.repository.BorrowRepository;
import com.example.borrow.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BorrowServiceTest {
    @Mock
    private BookInstanceRepository bookInstanceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BorrowRepository borrowRepository;

    @InjectMocks
    private BorrowService borrowService;

    @Test
    void borrowBook() {
        //GIVEN
        BookDefinitionEntity bookDefinitionEntity = new BookDefinitionEntity(UUID.randomUUID(), "test", "test");
        BookInstanceEntity bookInstanceEntity = new BookInstanceEntity(UUID.randomUUID(), BookState.OK, bookDefinitionEntity, 1L);
        bookDefinitionEntity.addBookInstance(bookInstanceEntity);

        UserEntity userEntity = new UserEntity(UUID.randomUUID());

        BorrowEntity borrowEntity = new BorrowEntity(UUID.randomUUID(), bookInstanceEntity, userEntity, BorrowStatus.ONGOING, LocalDateTime.now(), null);

        when(userRepository.findById(userEntity.getId()))
                .thenReturn(Optional.of(userEntity));
        when(borrowRepository.existsByBookInstanceId_AndStatusEquals(bookInstanceEntity.getId(), BorrowStatus.ONGOING))
                .thenReturn(false);
        when(bookInstanceRepository.findById(bookInstanceEntity.getId()))
                .thenReturn(Optional.of(bookInstanceEntity));
        when(borrowRepository.countBorrowedByUser_Id_AndStatusEquals(userEntity.getId(), BorrowStatus.ONGOING))
                .thenReturn(0);
        when(bookInstanceRepository.save(argThat(b -> b.getId() == bookInstanceEntity.getId())))
                .thenReturn(bookInstanceEntity);
        when(borrowRepository.save(argThat(b ->
                b.getBookInstance().getId() == bookInstanceEntity.getId() &&
                        b.getUser().getId() == userEntity.getId())))
                .thenReturn(borrowEntity);

        //WHEN
        Borrow result = borrowService.borrowBookAdmin(bookInstanceEntity.getId(), userEntity.getId());

        //THEN
        Assertions.assertEquals(borrowEntity.getId(), result.getId());
        Assertions.assertEquals(borrowEntity.getBookInstance().getId(), result.getBookInstance().getId());
        Assertions.assertEquals(borrowEntity.getUser().getId(), result.getUser().getId());
    }

    @Test
    void returnBook() {
        //GIVEN
        BookDefinitionEntity bookDefinitionEntity = new BookDefinitionEntity(UUID.randomUUID(), "test", "test");
        BookInstanceEntity bookInstanceEntity = new BookInstanceEntity(UUID.randomUUID(), BookState.OK, bookDefinitionEntity, 1L);
        bookDefinitionEntity.addBookInstance(bookInstanceEntity);

        UserEntity userEntity = new UserEntity(UUID.randomUUID());

        BorrowEntity borrowEntity_before = new BorrowEntity(UUID.randomUUID(), bookInstanceEntity, userEntity, BorrowStatus.ONGOING, LocalDateTime.now(), null);
        BorrowEntity borrowEntity_after = new BorrowEntity(borrowEntity_before.getId(), bookInstanceEntity, userEntity, BorrowStatus.RETURNED, LocalDateTime.now(), null);

        when(userRepository.findById(userEntity.getId()))
                .thenReturn(Optional.of(userEntity));
        when(borrowRepository.findById(borrowEntity_before.getId()))
                .thenReturn(Optional.of(borrowEntity_before));
        when(userRepository.existsById(borrowEntity_before.getUser().getId()))
                .thenReturn(true);
        when(bookInstanceRepository.existsById(borrowEntity_before.getBookInstance().getId()))
                .thenReturn(true);
        when(bookInstanceRepository.save(argThat(b -> b.getId() == bookInstanceEntity.getId())))
                .thenReturn(bookInstanceEntity);
        when(borrowRepository.save(argThat(b ->
                b.getBookInstance().getId() == bookInstanceEntity.getId() &&
                        b.getUser().getId() == userEntity.getId() &&
                        b.getStatus() == BorrowStatus.RETURNED)))
                .thenReturn(borrowEntity_after);

        //WHEN
        Borrow result = borrowService.returnBookAdmin(borrowEntity_before.getId(), userEntity.getId());

        //THEN
        Assertions.assertEquals(borrowEntity_after.getId(), result.getId());
        Assertions.assertEquals(borrowEntity_after.getStatus(), result.getStatus());
    }
}
