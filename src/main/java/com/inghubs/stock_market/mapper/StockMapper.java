package com.inghubs.stock_market.mapper;

import com.inghubs.stock_market.domain.Stock;
import com.inghubs.stock_market.dto.StockDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockDTO mapFromDomainToDto(Stock stock);
}
