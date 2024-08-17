package com.inghubs.stock_market.repository;

import com.inghubs.stock_market.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    boolean existsByName(String name);

    Optional<Stock> findByName(String name);
}
