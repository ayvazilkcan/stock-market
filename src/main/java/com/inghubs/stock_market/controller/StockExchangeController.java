package com.inghubs.stock_market.controller;

import com.inghubs.stock_market.dto.ApiResponse;
import com.inghubs.stock_market.dto.StockExchangeDTO;
import com.inghubs.stock_market.service.StockExchangeService;
import com.inghubs.stock_market.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock-exchanges")
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @Operation(summary = "Create a new stock exchange")
    @PostMapping
    public ResponseEntity<ApiResponse<StockExchangeDTO>> createStockExchange(@Valid @RequestBody StockExchangeDTO stockExchangeDTO) {
        StockExchangeDTO createdStockExchangeDTO = stockExchangeService.createStockExchange(stockExchangeDTO);

        ApiResponse<StockExchangeDTO> apiResponse = ApiResponse.<StockExchangeDTO>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .data(createdStockExchangeDTO)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @Operation(summary = "Get a stock exchange by name")
    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<StockExchangeDTO>> getStockExchange(@PathVariable @NotBlank String name) {
        StockExchangeDTO foundStockExchangeDTO = stockExchangeService.getStockExchangeByName(name);

        ApiResponse<StockExchangeDTO> apiResponse = ApiResponse.<StockExchangeDTO>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .data(foundStockExchangeDTO)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @Operation(summary = "Add a stock to a stock exchange by name")
    @PostMapping("/{stockExchangeName}/stocks/{stockName}")
    public ResponseEntity<ApiResponse<StockExchangeDTO>> addStockToStockExchange(@PathVariable @NotBlank String stockExchangeName, @PathVariable @NotBlank String stockName) {
        StockExchangeDTO stockExchangeDTO = stockExchangeService.addStockToStockExchangeByName(stockExchangeName, stockName);

        ApiResponse<StockExchangeDTO> apiResponse = ApiResponse.<StockExchangeDTO>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .data(stockExchangeDTO)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @Operation(summary = "Delete a stock from stock exchange by name")
    @DeleteMapping("/{stockExchangeName}/stocks/{stockName}")
    public ResponseEntity<ApiResponse<Void>> deleteStockFromStockExchange(@PathVariable @NotBlank String stockExchangeName, @PathVariable @NotBlank String stockName) {
        stockExchangeService.deleteStockFromStockExchangeByName(stockExchangeName, stockName);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }
}
