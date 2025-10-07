package com.example.borrow.controller;

import com.example.borrow.domain.model.Borrow;
import com.example.borrow.controller.dto.out.BorrowDto;
import com.example.borrow.domain.service.BorrowService;
import com.example.borrow.domain.mapper.BorrowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class BorrowController {
    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/borrow/{bookId}")
    public BorrowDto borrowBook(
            @PathVariable("bookId") UUID bookId
    ) {
        Borrow borrow = borrowService.borrowBook(bookId);
        return BorrowMapper.toDto(borrow);
    }

    @PostMapping("/admin/borrow/{bookId}/user/{userId}")
    public BorrowDto borrowBook(
            @PathVariable("bookId") UUID bookId,
            @PathVariable("userId") UUID userId
    ) {
        Borrow borrow = borrowService.borrowBookAdmin(bookId, userId);
        return BorrowMapper.toDto(borrow);
    }

    @PostMapping("/return/{borrowId}")
    public BorrowDto returnBook(
            @PathVariable("borrowId") UUID borrowId
    ) {
        Borrow borrow = borrowService.returnBook(borrowId);
        return BorrowMapper.toDto(borrow);
    }

    @PostMapping("/admin/return/{borrowId}/user/{userId}")
    public BorrowDto returnBook(
            @PathVariable("borrowId") UUID borrowId,
            @PathVariable("userId") UUID userId
    ) {
        Borrow borrow = borrowService.returnBookAdmin(borrowId, userId);
        return BorrowMapper.toDto(borrow);
    }

    @GetMapping("/user/borrow/history")
    public List<BorrowDto> getBorrowHistory() {
        List<Borrow> borrowList = borrowService.getBorrowHistory();
        return borrowList
                .stream()
                .map(BorrowMapper::toDto)
                .toList();
    }
}
