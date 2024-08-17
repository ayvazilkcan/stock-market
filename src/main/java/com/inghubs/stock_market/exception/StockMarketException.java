package com.inghubs.stock_market.exception;

import lombok.Getter;

@Getter
public class StockMarketException extends RuntimeException {

    private int errorCode;
    private String errorDetail;

    public StockMarketException(int errorCode, String errorDetail) {
        super(errorDetail);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }
}
