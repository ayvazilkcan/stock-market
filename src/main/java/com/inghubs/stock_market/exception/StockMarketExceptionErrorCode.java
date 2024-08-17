package com.inghubs.stock_market.exception;

public final class StockMarketExceptionErrorCode {
    private StockMarketExceptionErrorCode(){}

    public static final int STOCK_EXCHANGE_ALREADY_EXIST_ERROR_CODE = 1000;
    public static final int STOCK_EXCHANGE_NOT_FOUND_ERROR_CODE = 1001;
    public static final int STOCK_ALREADY_EXIST_ERROR_CODE = 1002;
    public static final int STOCK_NOT_FOUND_ERROR_CODE = 1003;
    public static final int SIMULTANEOUS_STOCK_PRICE_UPDATE_ERROR_CODE = 1004;
    public static final int STOCK_ALREADY_EXIST_IN_EXCHANGE_ERROR_CODE = 1005;
    public static final int STOCK_NOT_EXIST_IN_EXCHANGE_ERROR_CODE = 1006;
}
