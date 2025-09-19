package com.example.borrow.repository;

import com.example.borrow.persistence.repository.entity.BookDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDefinitionRepository extends JpaRepository<BookDefinitionEntity, Long> {
    List<BookDefinitionEntity> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author);
}
