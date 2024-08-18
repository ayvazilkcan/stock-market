package com.inghubs.stock_market.service;

import com.inghubs.stock_market.domain.Stock;
import com.inghubs.stock_market.domain.StockExchange;
import com.inghubs.stock_market.dto.StockDTO;
import com.inghubs.stock_market.dto.StockExchangeDTO;
import com.inghubs.stock_market.exception.StockMarketException;
import com.inghubs.stock_market.mapper.StockExchangeMapper;
import com.inghubs.stock_market.repository.StockExchangeRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.inghubs.stock_market.exception.StockMarketExceptionErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class StockExchangeServiceImplTest {

    @Mock
    private StockExchangeRepository stockExchangeRepository;

    @Mock
    private StockExchangeMapper stockExchangeMapper;

    @Mock
    private StockServiceImpl stockService;

    @InjectMocks
    private StockExchangeServiceImpl stockExchangeService;

    @Captor
    private ArgumentCaptor<StockExchange> stockExchangeArgumentCaptor;

    @Nested
    class CreateStockExchange {

        @Test
        void whenStockExchangeAlreadyExist_thenShouldThrowException() {
            StockExchangeDTO stockExchangeDTO = buildStockExchangeDTO();

            StockMarketException expectedException = new StockMarketException(STOCK_EXCHANGE_ALREADY_EXIST_ERROR_CODE, "Stock Exchange is already exist : " + stockExchangeDTO.getName());

            when(stockExchangeRepository.existsByName(stockExchangeDTO.getName())).thenReturn(true);

            StockMarketException actualException = assertThrows(StockMarketException.class, () -> stockExchangeService.createStockExchange(stockExchangeDTO));
            assertThat(actualException).usingRecursiveComparison().isEqualTo(expectedException);
        }

        @Test
        void whenStockExchangeNotExist_thenShouldCreate() {
            StockExchangeDTO stockExchangeDTO = buildStockExchangeDTO();
            StockExchange expectedStockExchange = buildStockExchange(stockExchangeDTO);
            StockExchangeDTO expectedStockExchangeDTO = buildStockExchangeDTO(expectedStockExchange);

            when(stockExchangeRepository.existsByName(stockExchangeDTO.getName())).thenReturn(false);
            when(stockExchangeRepository.save(any(StockExchange.class))).thenReturn(expectedStockExchange);
            when(stockExchangeMapper.mapFromDomainToDto(expectedStockExchange)).thenReturn(expectedStockExchangeDTO);

            StockExchangeDTO actualStockExchangeDTO = stockExchangeService.createStockExchange(stockExchangeDTO);

            verify(stockExchangeRepository).save(stockExchangeArgumentCaptor.capture());
            StockExchange actualStockExchange = stockExchangeArgumentCaptor.getValue();

            assertThat(actualStockExchange).usingRecursiveComparison().isEqualTo(expectedStockExchange);
            assertThat(actualStockExchangeDTO).usingRecursiveComparison().isEqualTo(expectedStockExchangeDTO);
        }
    }

    @Nested
    class GetStockExchangeByName {

        @Test
        void whenStockExchangeNotFound_thenShouldThrowException () {
            final String stockExchangeName = "name";

            StockMarketException expectedException = new StockMarketException(STOCK_EXCHANGE_NOT_FOUND_ERROR_CODE, "Stock Exchange is not found : " + stockExchangeName);

            when(stockExchangeRepository.findByName(stockExchangeName)).thenReturn(Optional.empty());

            StockMarketException actualException = assertThrows(StockMarketException.class, () -> stockExchangeService.getStockExchangeByName(stockExchangeName));
            assertThat(actualException).usingRecursiveComparison().isEqualTo(expectedException);
        }

        @Test
        void whenStockExchangeFound_thenShouldGet () {
            StockExchange foundStockExchange = buildStockExchange();
            StockExchangeDTO expectedStockExchangeDTO = buildStockExchangeDTO(foundStockExchange);
            StockExchange expectedStockExchange = buildStockExchange();

            when(stockExchangeRepository.findByName(foundStockExchange.getName())).thenReturn(Optional.of(foundStockExchange));
            when(stockExchangeMapper.mapFromDomainToDto(any(StockExchange.class))).thenReturn(expectedStockExchangeDTO);

            StockExchangeDTO actualStockExchangeDTO = stockExchangeService.getStockExchangeByName(foundStockExchange.getName());

            verify(stockExchangeMapper).mapFromDomainToDto(stockExchangeArgumentCaptor.capture());
            StockExchange actualStockExchange = stockExchangeArgumentCaptor.getValue();

            assertThat(actualStockExchangeDTO).usingRecursiveComparison().isEqualTo(expectedStockExchangeDTO);
            assertThat(actualStockExchange).usingRecursiveComparison().isEqualTo(expectedStockExchange);
        }
    }

    @Nested
    class AddStockToStockExchangeByName {

        @Test
        void whenStockAlreadyExistInExchange_thenShouldThrowException() {
            Stock existingStock = Stock.builder()
                    .name("name")
                    .description("desc")
                    .currentPrice(BigDecimal.TEN)
                    .build();

            Set<Stock> stocks = new HashSet<>();
            stocks.add(existingStock);

            StockExchange stockExchange = StockExchange.builder()
                    .name("name")
                    .description("desc")
                    .liveInMarket(false)
                    .stocks(stocks)
                    .build();

            Set<StockExchange> stockExchanges = new HashSet<>();
            stockExchanges.add(stockExchange);

            existingStock.setStockExchanges(stockExchanges);

            StockMarketException expectedException = new StockMarketException(STOCK_ALREADY_EXIST_IN_EXCHANGE_ERROR_CODE, "Stock already exist in exchange : " + existingStock.getName());

            when(stockExchangeRepository.findByName(stockExchange.getName())).thenReturn(Optional.of(stockExchange));
            when(stockService.getStockByName(existingStock.getName())).thenReturn(existingStock);

            StockMarketException actualException = assertThrows(StockMarketException.class, () -> stockExchangeService.addStockToStockExchangeByName(stockExchange.getName(), existingStock.getName()));

            assertThat(actualException).usingRecursiveComparison().isEqualTo(expectedException);
        }

        @Test
        void whenStockNotExistInExchange_thenShouldBeAdded() {
            Stock newStock = Stock.builder()
                    .name("newStock")
                    .description("desc")
                    .currentPrice(BigDecimal.ONE)
                    .build();

            Stock existingStock = Stock.builder()
                    .name("name")
                    .description("desc")
                    .currentPrice(BigDecimal.TEN)
                    .build();

            Set<Stock> existingStocks = new HashSet<>();
            existingStocks.add(existingStock);

            StockExchange foundStockExchange = StockExchange.builder()
                    .name("name")
                    .description("desc")
                    .liveInMarket(false)
                    .stocks(existingStocks)
                    .build();

            Set<Stock> expectedStocks = new HashSet<>();
            expectedStocks.add(existingStock);
            expectedStocks.add(newStock);

            StockExchange expectedStockExchange = StockExchange.builder()
                    .name("name")
                    .description("desc")
                    .liveInMarket(false)
                    .stocks(expectedStocks)
                    .build();

            StockExchangeDTO expectedStockExchangeDTO = buildStockExchangeDTO(expectedStockExchange);

            when(stockExchangeRepository.findByName(foundStockExchange.getName())).thenReturn(Optional.of(foundStockExchange));
            when(stockService.getStockByName(newStock.getName())).thenReturn(newStock);
            when(stockExchangeRepository.save(any(StockExchange.class))).thenReturn(expectedStockExchange);
            when(stockExchangeMapper.mapFromDomainToDto(expectedStockExchange)).thenReturn(expectedStockExchangeDTO);

            StockExchangeDTO actualStockExchangeDTO = stockExchangeService.addStockToStockExchangeByName(foundStockExchange.getName(), newStock.getName());

            verify(stockExchangeRepository).save(stockExchangeArgumentCaptor.capture());
            StockExchange actualStockExchange = stockExchangeArgumentCaptor.getValue();

            assertThat(actualStockExchangeDTO).usingRecursiveComparison().isEqualTo(expectedStockExchangeDTO);
            assertThat(actualStockExchange).usingRecursiveComparison().isEqualTo(expectedStockExchange);
        }
    }

    @Nested
    class DeleteStockFromStockExchangeByName {

        @Test
        void whenStockNotExistInExchange_thenShouldThrowException() {
            Stock stockToBeDeleted = Stock.builder()
                    .name("name")
                    .description("desc")
                    .currentPrice(BigDecimal.TEN)
                    .build();

            StockExchange stockExchange = StockExchange.builder()
                    .name("name")
                    .description("desc")
                    .liveInMarket(false)
                    .build();

            StockMarketException expectedException = new StockMarketException(STOCK_NOT_EXIST_IN_EXCHANGE_ERROR_CODE, "Stock not exist in exchange : " + stockToBeDeleted.getName());

            when(stockExchangeRepository.findByName(stockExchange.getName())).thenReturn(Optional.of(stockExchange));
            when(stockService.getStockByName(stockToBeDeleted.getName())).thenReturn(stockToBeDeleted);

            StockMarketException actualException = assertThrows(StockMarketException.class, () -> stockExchangeService.deleteStockFromStockExchangeByName(stockExchange.getName(), stockToBeDeleted.getName()));

            assertThat(actualException).usingRecursiveComparison().isEqualTo(expectedException);
        }

        @Test
        void whenStockExistInExchange_thenShouldBeDeleted() {
            Stock existingStock = Stock.builder()
                    .name("name")
                    .description("desc")
                    .currentPrice(BigDecimal.TEN)
                    .build();

            Set<Stock> existingStocks = new HashSet<>();
            existingStocks.add(existingStock);

            StockExchange foundStockExchange = StockExchange.builder()
                    .name("name")
                    .description("desc")
                    .liveInMarket(false)
                    .stocks(existingStocks)
                    .build();

            StockExchange expectedStockExchange = StockExchange.builder()
                    .name("name")
                    .description("desc")
                    .liveInMarket(false)
                    .stocks(Collections.emptySet())
                    .build();

            when(stockExchangeRepository.findByName(foundStockExchange.getName())).thenReturn(Optional.of(foundStockExchange));
            when(stockService.getStockByName(existingStock.getName())).thenReturn(existingStock);
            when(stockExchangeRepository.save(any(StockExchange.class))).thenReturn(expectedStockExchange);

            stockExchangeService.deleteStockFromStockExchangeByName(foundStockExchange.getName(), existingStock.getName());

            verify(stockExchangeRepository).save(stockExchangeArgumentCaptor.capture());
            StockExchange actualStockExchange = stockExchangeArgumentCaptor.getValue();

            assertThat(actualStockExchange).usingRecursiveComparison().isEqualTo(expectedStockExchange);
        }
    }

    private StockExchange buildStockExchange() {
        return StockExchange.builder()
                .name("name")
                .description("desc")
                .liveInMarket(false)
                .build();
    }

    private StockExchange buildStockExchange(StockExchangeDTO stockExchangeDTO) {
        return StockExchange.builder()
                .name(stockExchangeDTO.getName())
                .description(stockExchangeDTO.getDescription())
                .liveInMarket(stockExchangeDTO.isLiveInMarket())
                .build();
    }

    private StockExchangeDTO buildStockExchangeDTO() {
        return StockExchangeDTO.builder()
                .name("name")
                .description("desc")
                .liveInMarket(false)
                .build();
    }

    private StockDTO buildStockDTO(Stock stock) {
        return StockDTO.builder()
                .name(stock.getName())
                .description(stock.getDescription())
                .currentPrice(stock.getCurrentPrice())
                .build();
    }

    private StockExchangeDTO buildStockExchangeDTO(StockExchange stockExchange) {
        Set<StockDTO> stocks = new HashSet<>();

        if (!CollectionUtils.isEmpty(stockExchange.getStocks())) {
            stocks.addAll(stockExchange.getStocks().stream().map(this::buildStockDTO).collect(Collectors.toSet()));
        }

        return StockExchangeDTO.builder()
                .name(stockExchange.getName())
                .description(stockExchange.getDescription())
                .liveInMarket(stockExchange.isLiveInMarket())
                .stocks(stocks)
                .build();
    }
}
