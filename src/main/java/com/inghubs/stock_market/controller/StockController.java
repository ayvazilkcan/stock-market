package com.inghubs.stock_market.controller;

import com.inghubs.stock_market.dto.ApiResponse;
import com.inghubs.stock_market.dto.StockDTO;
import com.inghubs.stock_market.dto.StockPriceUpdateDTO;
import com.inghubs.stock_market.service.StockService;
import com.inghubs.stock_market.util.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stocks")
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<ApiResponse<StockDTO>> createStock(@Valid @RequestBody StockDTO stockDTO) {
        StockDTO createdStockDTO = stockService.createStock(stockDTO);

        ApiResponse<StockDTO> apiResponse = ApiResponse.<StockDTO>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .data(createdStockDTO)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PatchMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> updateStockPrice(@PathVariable @NotBlank String name, @Valid @RequestBody StockPriceUpdateDTO stockPriceUpdateDTO) {
        stockService.updateStockPrice(name, stockPriceUpdateDTO);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> deleteStock(@PathVariable @NotBlank String name) {
        stockService.deleteStockByName(name);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }
}
