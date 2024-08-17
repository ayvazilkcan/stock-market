package com.inghubs.stock_market.service;

import com.inghubs.stock_market.dto.StockExchangeDTO;

public interface StockExchangeService {
    StockExchangeDTO createStockExchange(StockExchangeDTO stockExchangeDTO);

    StockExchangeDTO getStockExchangeByName(String name);

    StockExchangeDTO addStockToStockExchangeByName(String stockExchangeName, String stockName);

    void deleteStockFromStockExchangeByName(String stockExchangeName, String stockName);
}
