package com.kadri.portfolio_service.service;

import com.kadri.portfolio_service.dto.HoldingDTO;
import com.kadri.portfolio_service.dto.TransactionRequestDTO;
import com.kadri.portfolio_service.entity.Holding;
import com.kadri.portfolio_service.entity.TransactionRecord;
import com.kadri.portfolio_service.entity.TransactionType;
import com.kadri.portfolio_service.repository.HoldingRepository;
import com.kadri.portfolio_service.repository.TransactionRepository;
import com.kadri.portfolio_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    @Transactional
    public TransactionRecord executeTransaction(TransactionRequestDTO request){
        verifyUserExists(request.getUserId());

        if(request.getQuantity() <= 0){
            throw new ResponseStatusException(BAD_REQUEST, "Quantity must be > 0");
        }
        TransactionRecord transaction = TransactionRecord.builder()
                .userId(request.getUserId())
                .stockSymbol(request.getStockSymbol())
                .type(request.getType())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .timestamp(OffsetDateTime.now())
                .build();

        Holding holding = holdingRepository.findByUserIdAndStockSymbol(request.getUserId(), request.getStockSymbol()).orElse(null);
        if(transaction.getType() == TransactionType.BUY){
            if(holding == null){
                Holding newHolding = Holding.builder()
                        .userId(request.getUserId())
                        .stockSymbol(request.getStockSymbol())
                        .quantity(request.getQuantity())
                        .averagePrice(request.getPrice())
                        .build();
                holdingRepository.save(newHolding);
            }else {
                BigDecimal oldSubTotal = holding.getAveragePrice().multiply(BigDecimal.valueOf(holding.getQuantity()));
                BigDecimal newSubTotal = request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
                Long newQuantity = holding.getQuantity() + request.getQuantity();
                BigDecimal newAvg = oldSubTotal.add(newSubTotal).divide(BigDecimal.valueOf(newQuantity), 8, RoundingMode.HALF_UP);
                holding.setQuantity(newQuantity);
                holding.setAveragePrice(newAvg);
                holdingRepository.save(holding);
            }
        } else if (transaction.getType() == TransactionType.SELL) {
            if (holding == null || holding.getQuantity() <= request.getQuantity()) {
                throw new ResponseStatusException(BAD_REQUEST, "Insufficient shares to sell");
            }
            Long newQuantity = holding.getQuantity() - request.getQuantity();
            if(newQuantity == 0L){
                holdingRepository.delete(holding);
            }else {
                holding.setQuantity(newQuantity);
                holdingRepository.save(holding);
            }
        }else {
            throw new ResponseStatusException(BAD_REQUEST, "Unsupported transaction type");
        }
        return transactionRepository.save(transaction);
    }

    public BigDecimal calculateSimulatedPortfolioValue(Long userId){
        verifyUserExists(userId);
        return holdingRepository.findByUserId(userId).stream()
                .map(holding -> holding.getAveragePrice().multiply(BigDecimal.valueOf(holding.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void verifyUserExists(Long userId) {
        if(!(userRepository.existsById(userId))){
            throw new ResponseStatusException(NOT_FOUND, "User not found: " + userId);
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
