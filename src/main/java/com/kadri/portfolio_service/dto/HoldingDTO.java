package com.kadri.portfolio_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class HoldingDTO {
    private Long id;
    private Long userId;
    private String stockSymbol;
    private Long quantity;
    private BigDecimal averagePrice;
}
