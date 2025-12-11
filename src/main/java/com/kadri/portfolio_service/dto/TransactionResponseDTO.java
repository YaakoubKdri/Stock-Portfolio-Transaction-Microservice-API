package com.kadri.portfolio_service.dto;

import com.kadri.portfolio_service.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TransactionResponseDTO {
    private Long id;
    private Long userId;
    private String stockSymbol;
    private TransactionType type;
    private Long quantity;
    private BigDecimal price;
    private OffsetDateTime timestamp;
}
