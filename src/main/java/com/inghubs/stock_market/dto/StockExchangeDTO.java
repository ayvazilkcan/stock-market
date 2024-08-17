package com.inghubs.stock_market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockExchangeDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean liveInMarket;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<StockDTO> stocks;
}
