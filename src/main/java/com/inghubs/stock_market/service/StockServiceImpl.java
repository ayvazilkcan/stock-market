package com.inghubs.stock_market.service;

import com.inghubs.stock_market.domain.Stock;
import com.inghubs.stock_market.dto.StockDTO;
import com.inghubs.stock_market.dto.StockPriceUpdateDTO;
import com.inghubs.stock_market.exception.StockMarketException;
import com.inghubs.stock_market.mapper.StockMapper;
import com.inghubs.stock_market.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.inghubs.stock_market.exception.StockMarketExceptionErrorCode.STOCK_ALREADY_EXIST_ERROR_CODE;
import static com.inghubs.stock_market.exception.StockMarketExceptionErrorCode.STOCK_NOT_FOUND_ERROR_CODE;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockDTO createStock(StockDTO stockDTO) {
        boolean alreadyExist = stockRepository.existsByName(stockDTO.getName());

        if (alreadyExist) {
            throw new StockMarketException(STOCK_ALREADY_EXIST_ERROR_CODE, "Stock is already exist : " + stockDTO.getName());
        }

        Stock stock = Stock.builder()
                .name(stockDTO.getName())
                .description(stockDTO.getDescription())
                .currentPrice(stockDTO.getCurrentPrice())
                .build();

        return stockMapper.mapFromDomainToDto(stockRepository.save(stock));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void updateStockPrice(String name, StockPriceUpdateDTO stockPriceUpdateDTO) {
        Stock stock = getStockByName(name);

        stock.setCurrentPrice(stockPriceUpdateDTO.getPrice());

        stockRepository.save(stock);
    }

    @Override
    public Stock getStockByName(String name) {
        return stockRepository.findByName(name).orElseThrow(() -> new StockMarketException(STOCK_NOT_FOUND_ERROR_CODE, "Stock is not found : " + name));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStockByName(String name) {
        Stock stock = getStockByName(name);

        Optional.ofNullable(stock.getStockExchanges()).orElse(Collections.emptySet()).forEach(stockExchange -> {
            stockExchange.removeStock(stock);
            stockExchange.updateLiveInMarket();
        });

        stockRepository.deleteById(stock.getId());
    }
}
