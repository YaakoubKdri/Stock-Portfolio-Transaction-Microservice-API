package com.kadri.portfolio_service.repository;

import com.kadri.portfolio_service.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    List<Holding> findByUserId(Long useId);
    Optional<Holding> findByUserIdAndStockSymbol(Long userId, String stockSymbol);
}
