package com.inghubs.stock_market.service;

import com.inghubs.stock_market.domain.Stock;
import com.inghubs.stock_market.domain.StockExchange;
import com.inghubs.stock_market.dto.StockDTO;
import com.inghubs.stock_market.dto.StockPriceUpdateDTO;
import com.inghubs.stock_market.exception.StockMarketException;
import com.inghubs.stock_market.mapper.StockMapper;
import com.inghubs.stock_market.repository.StockRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.inghubs.stock_market.exception.StockMarketExceptionErrorCode.STOCK_ALREADY_EXIST_ERROR_CODE;
import static com.inghubs.stock_market.exception.StockMarketExceptionErrorCode.STOCK_NOT_FOUND_ERROR_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockServiceImpl stockService;

    @Captor
    private ArgumentCaptor<Stock> stockArgumentCaptor;

    @Nested
    class CreateStock {

        @Test
        void whenStockAlreadyExist_thenShouldThrowException() {
            StockDTO stockDTO = buildStockDTO();

            StockMarketException expectedException = new StockMarketException(STOCK_ALREADY_EXIST_ERROR_CODE, "Stock is already exist : " + stockDTO.getName());

            when(stockRepository.existsByName(stockDTO.getName())).thenReturn(true);

            StockMarketException actualException = assertThrows(StockMarketException.class, () -> stockService.createStock(stockDTO));
            assertThat(actualException).usingRecursiveComparison().isEqualTo(expectedException);
        }

        @Test
        void whenStockNotExist_thenShouldCreate() {
            StockDTO stockDTO = buildStockDTO();
            StockDTO expectedStockDTO = buildStockDTO();
            Stock expectedStock = buildStock(stockDTO);

            when(stockRepository.existsByName(stockDTO.getName())).thenReturn(false);
            when(stockRepository.save(any(Stock.class))).thenReturn(expectedStock);
            when(stockMapper.mapFromDomainToDto(expectedStock)).thenReturn(expectedStockDTO);

            StockDTO actualStockDTO = stockService.createStock(stockDTO);

            verify(stockRepository).save(stockArgumentCaptor.capture());
            Stock actualStock = stockArgumentCaptor.getValue();
            assertThat(actualStockDTO).usingRecursiveComparison().isEqualTo(expectedStockDTO);
            assertThat(actualStock).usingRecursiveComparison().isEqualTo(expectedStock);
        }

    }

    @Nested
    class UpdateStockPrice {

        private final String stockName = "stockName";

        @Test
        void whenStockNotFound_thenShouldThrowException() {
            StockPriceUpdateDTO stockPriceUpdateDTO = buildStockPriceUpdateDTO();

            when(stockRepository.findByName(stockName)).thenReturn(Optional.empty());

            StockMarketException expectedException = new StockMarketException(STOCK_NOT_FOUND_ERROR_CODE, "Stock is not found : " + stockName);

            StockMarketException actualException = assertThrows(StockMarketException.class, () -> stockService.updateStockPrice(stockName, stockPriceUpdateDTO));
            assertThat(actualException).usingRecursiveComparison().isEqualTo(expectedException);
        }

        @Test
        void whenStockFound_thenShouldUpdateStockPrice() {
            StockPriceUpdateDTO stockPriceUpdateDTO = buildStockPriceUpdateDTO();
            Stock foundStock = buildStock(stockName);
            Stock expectedStock = buildStock(stockName);
            expectedStock.setCurrentPrice(stockPriceUpdateDTO.getPrice());

            when(stockRepository.findByName(stockName)).thenReturn(Optional.of(foundStock));
            when(stockRepository.save(any(Stock.class))).thenReturn(expectedStock);

            stockService.updateStockPrice(stockName, stockPriceUpdateDTO);

            verify(stockRepository).save(stockArgumentCaptor.capture());
            Stock actualStock = stockArgumentCaptor.getValue();

            assertThat(actualStock).usingRecursiveComparison().isEqualTo(expectedStock);
        }

        private StockPriceUpdateDTO buildStockPriceUpdateDTO() {
            return StockPriceUpdateDTO.builder()
                    .price(BigDecimal.ONE)
                    .build();
        }
    }

    @Nested
    class DeleteStockByName {

        @Test
        void whenStockFound_thenShouldDelete() {
            Stock foundStock = Stock.builder()
                    .id(1L)
                    .name("name")
                    .description("desc")
                    .currentPrice(BigDecimal.TEN)
                    .build();

            Set<Stock> stocks = new HashSet<>();
            stocks.add(foundStock);

            StockExchange stockExchange = StockExchange.builder()
                    .name("name")
                    .description("desc")
                    .liveInMarket(false)
                    .stocks(stocks)
                    .build();

            Set<StockExchange> stockExchanges = new HashSet<>();
            stockExchanges.add(stockExchange);

            foundStock.setStockExchanges(stockExchanges);

            when(stockRepository.findByName(foundStock.getName())).thenReturn(Optional.of(foundStock));

            stockService.deleteStockByName(foundStock.getName());

            verify(stockRepository).deleteById(foundStock.getId());
            assertThat(stockExchange.getStocks()).isEmpty();
            assertThat(stockExchange.isLiveInMarket()).isFalse();
        }
    }

    private StockDTO buildStockDTO() {
        return StockDTO.builder()
                .name("stockName")
                .description("desc")
                .currentPrice(BigDecimal.TEN)
                .build();
    }

    private Stock buildStock(String name) {
        return Stock.builder()
                .name(name)
                .description("desc")
                .currentPrice(BigDecimal.TEN)
                .build();
    }

    private Stock buildStock(StockDTO stockDTO) {
        return Stock.builder()
                .name(stockDTO.getName())
                .description(stockDTO.getDescription())
                .currentPrice(stockDTO.getCurrentPrice())
                .build();
    }
}
