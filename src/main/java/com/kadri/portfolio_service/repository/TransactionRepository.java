package com.kadri.portfolio_service.repository;

import com.kadri.portfolio_service.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {
    List<TransactionRecord> findByUserIdOrderByTimestampDesc(Long userId);
}
