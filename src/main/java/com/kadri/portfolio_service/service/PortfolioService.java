package com.kadri.portfolio_service.service;

import com.kadri.portfolio_service.dto.HoldingDTO;
import com.kadri.portfolio_service.entity.Holding;
import com.kadri.portfolio_service.entity.TransactionRecord;
import com.kadri.portfolio_service.repository.HoldingRepository;
import com.kadri.portfolio_service.repository.TransactionRepository;
import com.kadri.portfolio_service.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    private final HoldingRepository holdingRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public PortfolioService(HoldingRepository holdingRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.holdingRepository = holdingRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<HoldingDTO> getHoldingsForUser(Long userId){
        verifyUserExists(userId);
        return holdingRepository.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TransactionRecord> getTransactionsForUser(Long userId){
        verifyUserExists(userId);
        return transactionRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    private void verifyUserExists(Long userId) {
        if(!(userRepository.existsById(userId))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId);
        }
    }

    private HoldingDTO toDto(Holding holding) {
        return HoldingDTO.builder()
                .id(holding.getId())
                .userId(holding.getUserId())
                .stockSymbol(holding.getStockSymbol())
                .quantity(holding.getQuantity())
                .averagePrice(holding.getAveragePrice())
                .build();
    }
}
