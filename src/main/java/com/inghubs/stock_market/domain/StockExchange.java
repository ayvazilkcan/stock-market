package com.inghubs.stock_market.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Set;

import static com.inghubs.stock_market.util.Constants.MIN_STOCK_COUNT_FOR_BEING_LIVE_IN_MARKET;
import static java.util.Collections.emptySet;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = @Index(columnList = "name", unique = true))
public class StockExchange extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "live_in_market", nullable = false)
    private boolean liveInMarket;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "stock_market",
            joinColumns = @JoinColumn(name = "stock_exchange_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id"))
    private Set<Stock> stocks;

    public void addStock(Stock stock) {
        if (this.stocks == null) {
            this.stocks = Set.of(stock);
            return;
        }

        this.stocks.add(stock);
    }

    public void updateLiveInMarket() {
        if (CollectionUtils.isEmpty(this.stocks)) {
            return;
        }

        if (this.stocks.size() < MIN_STOCK_COUNT_FOR_BEING_LIVE_IN_MARKET) {
            this.liveInMarket = false;
            return;
        }

        this.liveInMarket = true;
    }

    public void removeStock(Stock stock) {
        if (CollectionUtils.isEmpty(this.stocks)) {
            return;
        }

        this.stocks.remove(stock);
    }
}
