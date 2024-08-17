package com.inghubs.stock_market.mapper;

import com.inghubs.stock_market.domain.StockExchange;
import com.inghubs.stock_market.dto.StockExchangeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = StockMapper.class)
public interface StockExchangeMapper {

    StockExchangeDTO mapFromDomainToDto(StockExchange stockExchange);
}
