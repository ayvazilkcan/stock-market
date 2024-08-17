package com.inghubs.stock_market.service;

import com.inghubs.stock_market.domain.Stock;
import com.inghubs.stock_market.dto.StockDTO;
import com.inghubs.stock_market.dto.StockPriceUpdateDTO;

public interface StockService {

    StockDTO createStock(StockDTO stockDTO);

    void updateStockPrice(String name, StockPriceUpdateDTO stockPriceUpdateDTO);

    Stock getStockByName(String name);

    void deleteStockByName(String name);
}
