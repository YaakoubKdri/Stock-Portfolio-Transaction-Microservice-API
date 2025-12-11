package com.kadri.portfolio_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HoldingDTO {
    private Long id;
    private Long userId;
    private String stockSymbol;
    private Long quantity;
    private BigDecimal averagePrice;
}
