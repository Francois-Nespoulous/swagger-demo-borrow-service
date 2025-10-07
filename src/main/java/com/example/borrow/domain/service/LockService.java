package com.example.borrow.domain.service;

import com.example.borrow.domain.model.Lock;
import com.example.borrow.domain.mapper.LockMapper;
import com.example.borrow.repository.LockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class LockService {
    private final LockRepository lockRepository;

    public LockService(LockRepository lockRepository) {
        this.lockRepository = lockRepository;
    }

    @Transactional
    public boolean tryLock(UUID bookInstanceId, UUID userId) {
        boolean bookInstanceAlreadyLocked = lockRepository.existsByBookInstanceId(bookInstanceId);
        boolean userAlreadyLocked = lockRepository.existsByUserId(userId);
        if (bookInstanceAlreadyLocked || userAlreadyLocked) {
            return false;
        }

        Lock lock = new Lock(bookInstanceId, userId, Instant.now());
        lockRepository.save(LockMapper.toEntity(lock));
        return true;
    }

    @Transactional
    public void unlock(UUID bookInstanceId) {
        lockRepository.deleteByBookInstanceId(bookInstanceId);
    }
}
