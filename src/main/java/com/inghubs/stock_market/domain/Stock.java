package com.inghubs.stock_market.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = @Index(columnList = "name", unique = true))
public class Stock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;
    @ManyToMany(mappedBy = "stocks")
    private Set<StockExchange> stockExchanges;
}
