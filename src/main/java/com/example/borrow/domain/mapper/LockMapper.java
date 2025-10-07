package com.example.borrow.domain.mapper;

import com.example.borrow.domain.model.Lock;
import com.example.borrow.persistence.repository.entity.LockEntity;

public class LockMapper {
    public static LockEntity toEntity(Lock lock) {
        return new LockEntity(
                lock.getBookInstanceId(),
                lock.getUserId(),
                lock.getLockedAt()
        );
    }
}
