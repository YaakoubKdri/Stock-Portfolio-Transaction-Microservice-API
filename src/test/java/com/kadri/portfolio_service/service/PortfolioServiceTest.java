package com.kadri.portfolio_service.service;

import com.kadri.portfolio_service.dto.TransactionRequestDTO;
import com.kadri.portfolio_service.entity.Holding;
import com.kadri.portfolio_service.entity.TransactionRecord;
import com.kadri.portfolio_service.entity.TransactionType;
import com.kadri.portfolio_service.repository.HoldingRepository;
import com.kadri.portfolio_service.repository.TransactionRepository;
import com.kadri.portfolio_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PortfolioServiceTest {

    @Mock
    private HoldingRepository holdingRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PortfolioService service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buy_createsOrUpdatesHoldings_and_recordsTransaction(){
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .userId(userId)
                .stockSymbol("Apple")
                .quantity(10L)
                .type(TransactionType.BUY)
                .price(new BigDecimal("150"))
                .build();
        when(holdingRepository.findByUserIdAndStockSymbol(userId, "Apple")).thenReturn(Optional.empty());
        when(transactionRepository.save(any(TransactionRecord.class))).thenAnswer(inv ->{
            TransactionRecord transaction = inv.getArgument(0);
            transaction.setId(100L);
            return transaction;
        });

        service.executeTransaction(request);

        verify(holdingRepository, times(1)).save(any(Holding.class));
        verify(transactionRepository, times(1)).save(any(TransactionRecord.class));
    }

    @Test
    void sell_withInsufficientShares_throws(){
        Long userId = 2L;
        when(userRepository.existsById(2L)).thenReturn(true);

        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .userId(userId)
                .stockSymbol("Google")
                .type(TransactionType.SELL)
                .quantity(5L)
                .price(new BigDecimal("2500"))
                .build();
        when(holdingRepository.findByUserIdAndStockSymbol(userId, "Google")).thenReturn(Optional.of(Holding.builder()
                .userId(userId)
                .stockSymbol("Google")
                .quantity(2L)
                .averagePrice(new BigDecimal("2000"))
                .build()));

        try {
            service.executeTransaction(request);
            throw new AssertionError("Expected exception");
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        verify(transactionRepository, never()).save(any());
    }
}
