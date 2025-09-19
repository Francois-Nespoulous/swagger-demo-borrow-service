package com.example.borrow.util;

import com.example.borrow.domain.enums.BorrowStatus;
import com.example.borrow.persistence.repository.entity.BookInstanceEntity;
import com.example.borrow.repository.BorrowRepository;

import java.util.List;

public class BookStatsUtil {
    public static BookStats computeStats(List<BookInstanceEntity> bookInstances, BorrowRepository borrowRepository) {
        int nbTotalBooks = bookInstances.size();
        int nbBorrowedBooks = (int) bookInstances
                .stream()
                .filter(instance -> borrowRepository.existsByBookInstanceId_AndStatusEquals(instance.getId(), BorrowStatus.ONGOING))
                .count();
        return new BookStats(nbTotalBooks, nbBorrowedBooks);
    }

    public record BookStats(int nbTotalBooks, int nbBorrowedBooks) {}
}
