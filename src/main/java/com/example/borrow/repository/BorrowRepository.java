package com.example.borrow.repository;

import com.example.borrow.domain.enums.BorrowStatus;
import com.example.borrow.persistence.repository.entity.BorrowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BorrowRepository extends JpaRepository<BorrowEntity, Long> {
    List<BorrowEntity> getAllByUser_Id(UUID id);

    boolean existsByBookInstanceId_AndStatusEquals(UUID bookInstanceId, BorrowStatus borrowStatus);

    Optional<BorrowEntity> findById(UUID borrowId);

    int countBorrowedByUser_Id_AndStatusEquals(UUID userId, BorrowStatus borrowStatus);
}
