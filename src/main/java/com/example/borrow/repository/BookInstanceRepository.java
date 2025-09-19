package com.example.borrow.repository;

import com.example.borrow.persistence.repository.entity.BookInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceRepository extends JpaRepository<BookInstanceEntity, Long> {
    List<BookInstanceEntity> findByBookDefinitionId(UUID bookDefinitionId);

    Optional<BookInstanceEntity> findById(UUID bookInstanceId);

    boolean existsById(UUID id);
}
