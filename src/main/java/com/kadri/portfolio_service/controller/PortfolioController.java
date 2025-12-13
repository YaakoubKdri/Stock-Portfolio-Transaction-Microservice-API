package com.kadri.portfolio_service.controller;

import com.kadri.portfolio_service.dto.HoldingDTO;
import com.kadri.portfolio_service.dto.TransactionRequestDTO;
import com.kadri.portfolio_service.dto.TransactionResponseDTO;
import com.kadri.portfolio_service.entity.TransactionRecord;
import com.kadri.portfolio_service.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/portfolio")
@Tag(name = "Portfolio", description = "Endpoints for portfolio holdings and transactions")
public class PortfolioController {
    private final PortfolioService service;

    public PortfolioController(PortfolioService service) {
        this.service = service;
    }

    @GetMapping("/{userId}/holdings")
    @Operation(summary = "Get holdings for a user", description = "Returns current holdings for a user")
    public ResponseEntity<List<HoldingDTO>> getHoldings(@PathVariable Long userId){
        List<HoldingDTO> holdings = service.getHoldingsForUser(userId);
        return ResponseEntity.ok(holdings);
    }

    @PostMapping("transaction")
    @Operation(summary = "Execute a BUY or SELL transaction")
    public ResponseEntity<TransactionResponseDTO> executeTransaction(@Valid @RequestBody TransactionRequestDTO request){
        var response = toResponse(service.executeTransaction(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}/transactions")
    @Operation(summary = "Get transaction history for a user")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(@PathVariable Long userId){
        List<TransactionRecord> transactions = service.getTransactionsForUser(userId);
        List<TransactionResponseDTO> transactionResponseDTOS = transactions.stream()
                .map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(transactionResponseDTOS);
    }

    @GetMapping("/{userId}/metrics")
    @Operation(summary = "Get simulated portfolio total value")
    public ResponseEntity<BigDecimal> getMetrics(@PathVariable Long userId){
        BigDecimal total = service.calculateSimulatedPortfolioValue(userId);
        return ResponseEntity.ok(total);
    }

    private TransactionResponseDTO toResponse(TransactionRecord transactionRecord) {
        return TransactionResponseDTO.builder()
                .id(transactionRecord.getId())
                .userId(transactionRecord.getUserId())
                .stockSymbol(transactionRecord.getStockSymbol())
                .type(transactionRecord.getType())
                .quantity(transactionRecord.getQuantity())
                .price(transactionRecord.getPrice())
                .timestamp(transactionRecord.getTimestamp())
                .build();
    }
}
