package com.inghubs.stock_market.service;

import com.inghubs.stock_market.domain.Stock;
import com.inghubs.stock_market.domain.StockExchange;
import com.inghubs.stock_market.dto.StockExchangeDTO;
import com.inghubs.stock_market.exception.StockMarketException;
import com.inghubs.stock_market.mapper.StockExchangeMapper;
import com.inghubs.stock_market.repository.StockExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.inghubs.stock_market.exception.StockMarketExceptionErrorCode.*;

@Service
@RequiredArgsConstructor
public class StockExchangeServiceImpl implements StockExchangeService {

    private final StockExchangeRepository stockExchangeRepository;
    private final StockExchangeMapper stockExchangeMapper;
    private final StockService stockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockExchangeDTO createStockExchange(StockExchangeDTO stockExchangeDTO) {
        boolean alreadyExist = stockExchangeRepository.existsByName(stockExchangeDTO.getName());

        if (alreadyExist) {
            throw new StockMarketException(STOCK_EXCHANGE_ALREADY_EXIST_ERROR_CODE, "Stock Exchange is already exist : " + stockExchangeDTO.getName());
        }

        StockExchange stockExchange = StockExchange.builder()
                .name(stockExchangeDTO.getName())
                .description(stockExchangeDTO.getDescription())
                .liveInMarket(false)
                .build();

        return stockExchangeMapper.mapFromDomainToDto(stockExchangeRepository.save(stockExchange));
    }

    @Override
    public StockExchangeDTO getStockExchangeByName(String name) {
        StockExchange stockExchange = getStockExchange(name);
        return stockExchangeMapper.mapFromDomainToDto(stockExchange);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockExchangeDTO addStockToStockExchangeByName(String stockExchangeName, String stockName) {
        StockExchange stockExchange = getStockExchange(stockExchangeName);

        Stock stock = stockService.getStockByName(stockName);

        Optional<Stock> foundStockInExchange = stockExchange.getStocks().stream().filter(item -> item.getName().equals(stock.getName())).findFirst();

        if (foundStockInExchange.isPresent()) {
            throw new StockMarketException(STOCK_ALREADY_EXIST_IN_EXCHANGE_ERROR_CODE, "Stock already exist in exchange : " + stockName);
        }

        stockExchange.addStock(stock);
        stockExchange.updateLiveInMarket();

        return stockExchangeMapper.mapFromDomainToDto(stockExchangeRepository.save(stockExchange));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStockFromStockExchangeByName(String stockExchangeName, String stockName) {
        StockExchange stockExchange = getStockExchange(stockExchangeName);

        Stock stock = stockService.getStockByName(stockName);

        Optional<Stock> foundStockInExchange = Optional.ofNullable(stockExchange.getStocks()).orElse(Collections.emptySet()).stream().filter(item -> item.getName().equals(stock.getName())).findFirst();

        if (foundStockInExchange.isEmpty()) {
            throw new StockMarketException(STOCK_NOT_EXIST_IN_EXCHANGE_ERROR_CODE, "Stock not exist in exchange : " + stockName);
        }

        stockExchange.removeStock(stock);
        stockExchange.updateLiveInMarket();

        stockExchangeRepository.save(stockExchange);
    }

    private StockExchange getStockExchange(String name) {
        return stockExchangeRepository.findByName(name).orElseThrow(() -> new StockMarketException(STOCK_EXCHANGE_NOT_FOUND_ERROR_CODE, "Stock Exchange is not found : " + name));
    }
}
