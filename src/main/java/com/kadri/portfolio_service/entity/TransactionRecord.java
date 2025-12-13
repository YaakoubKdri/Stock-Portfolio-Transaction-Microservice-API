package com.kadri.portfolio_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "stock_symbol", nullable = false)
    private String stockSymbol;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private Long quantity;

    @Column(precision = 19, scale = 4)
    private BigDecimal price;
    private OffsetDateTime timestamp;
}
