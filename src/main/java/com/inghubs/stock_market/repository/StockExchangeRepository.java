package com.inghubs.stock_market.repository;

import com.inghubs.stock_market.domain.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {

    boolean existsByName(String name);

    Optional<StockExchange> findByName(String name);
}
