package com.example.borrow.service;

import com.example.borrow.domain.service.LockService;
import com.example.borrow.repository.LockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
public class LockServiceTest {
    @Autowired
    private LockService lockService;
    @Autowired
    private LockRepository lockRepository;

    @Test
    void testOptimisticLock() {
        UUID bookInstanceId = UUID.fromString("22222222-0000-0000-0000-000000000001");
        UUID anotherBookInstanceId = UUID.fromString("22222222-0000-0000-0000-000000000002");

        UUID userId = UUID.fromString("33333333-1111-1111-1111-111111111101");
        UUID anotherUserId = UUID.fromString("33333333-1111-1111-1111-111111111102");

        //OK first time
        Assertions.assertTrue(lockService.tryLock(bookInstanceId, userId));
        //KO same book instance & same user
        Assertions.assertFalse(lockService.tryLock(bookInstanceId, userId));
        //KO same book instance
        Assertions.assertFalse(lockService.tryLock(bookInstanceId, anotherUserId));
        //KO same user
        Assertions.assertFalse(lockService.tryLock(anotherBookInstanceId, userId));
        //OK different values
        Assertions.assertTrue(lockService.tryLock(anotherBookInstanceId, anotherUserId));

        //Only 2 were OK
        Assertions.assertEquals(2, lockRepository.count());
    }
}
