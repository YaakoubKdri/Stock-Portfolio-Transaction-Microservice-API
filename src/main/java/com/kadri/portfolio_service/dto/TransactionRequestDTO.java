package com.kadri.portfolio_service.dto;

import com.kadri.portfolio_service.entity.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequestDTO {

    @NotNull
    private Long userId;

    @NotNull
    private String stockSymbol;

    @NotNull
    private TransactionType type;

    @NotNull
    @Min(1)
    private Long quantity;

    @NotNull
    private BigDecimal price;
}
