package com.inghubs.stock_market.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.inghubs.stock_market.exception.StockMarketExceptionErrorCode.*;
import static com.inghubs.stock_market.exception.StockMarketExceptionMessage.*;

@Getter
public enum StockMarketExceptionType {

    STOCK_EXCHANGE_ALREADY_EXIST(STOCK_EXCHANGE_ALREADY_EXIST_ERROR_CODE, STOCK_EXCHANGE_ALREADY_EXIST_MSG, HttpStatus.BAD_REQUEST),
    STOCK_EXCHANGE_NOT_FOUND(STOCK_EXCHANGE_NOT_FOUND_ERROR_CODE, STOCK_EXCHANGE_NOT_FOUND_MSG, HttpStatus.NOT_FOUND),
    STOCK_ALREADY_EXIST(STOCK_ALREADY_EXIST_ERROR_CODE, STOCK_ALREADY_EXIST_MSG, HttpStatus.BAD_REQUEST),
    STOCK_NOT_FOUND(STOCK_NOT_FOUND_ERROR_CODE, STOCK_NOT_FOUND_MSG, HttpStatus.NOT_FOUND),
    STOCK_ALREADY_EXIST_IN_EXCHANGE(STOCK_ALREADY_EXIST_IN_EXCHANGE_ERROR_CODE, STOCK_ALREADY_EXIST_IN_EXCHANGE_MSG, HttpStatus.BAD_REQUEST),
    STOCK_NOT_EXIST_IN_EXCHANGE(STOCK_NOT_EXIST_IN_EXCHANGE_ERROR_CODE, STOCK_NOT_EXIST_IN_EXCHANGE_MSG, HttpStatus.BAD_REQUEST);

    private int errorCode;
    private String errorMessage;
    private HttpStatus status;

    private static final Map<Integer, StockMarketExceptionType> enumMap = new HashMap<>();

    StockMarketExceptionType(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    static {
        Arrays.stream(values()).forEach(item -> enumMap.put(item.getErrorCode(), item));
    }

    public static StockMarketExceptionType getExceptionByErrorCode(int errorCode) {
        return enumMap.get(errorCode);
    }
}
