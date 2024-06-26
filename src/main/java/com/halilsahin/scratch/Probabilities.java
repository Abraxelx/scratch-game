package com.halilsahin.scratch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Probabilities {
    @JsonProperty("standard_symbols")
    private List<Probability> standardSymbols;

    @JsonProperty("bonus_symbols")
    private BonusSymbols bonusSymbols;

    @Data
    public static class BonusSymbols {
        @JsonProperty("symbols")
        private Map<String, Integer> symbols;
    }
}
