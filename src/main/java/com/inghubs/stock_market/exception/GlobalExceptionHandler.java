package com.inghubs.stock_market.exception;

import com.inghubs.stock_market.dto.ApiResponse;
import com.inghubs.stock_market.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StockMarketException.class)
    public ResponseEntity<ApiResponse<Object>> handleBookStoreException(StockMarketException ex) {
        StockMarketExceptionType exception = StockMarketExceptionType.getExceptionByErrorCode(ex.getErrorCode());

        ApiResponse.Error error = ApiResponse.Error.builder()
                .code(exception.getErrorCode())
                .message(exception.getErrorMessage())
                .detail(ex.getErrorDetail())
                .build();

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .error(error)
                .message(Constants.ERROR_RESPONSE_MESSAGE)
                .build();

        return ResponseEntity.status(exception.getStatus()).body(response);
    }
}
