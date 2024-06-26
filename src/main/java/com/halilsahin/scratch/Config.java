package com.halilsahin.scratch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    @JsonProperty("rows")
    private int rows;

    @JsonProperty("columns")
    private int columns;

    @JsonProperty("symbols")
    private Map<String, Symbol> symbols;

    @JsonProperty("probabilities")
    private Probabilities probabilities;

    @JsonProperty("win_combinations")
    private Map<String, WinCombination> winCombinations;

    public static Config load(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), Config.class);
    }

    @Data
    public static class Symbol {
        @JsonProperty("reward_multiplier")
        private double rewardMultiplier;
        @JsonProperty("type")
        private String type;
        @JsonProperty("impact")
        private String impact;
        @JsonProperty("extra")
        private Integer extra;
    }

    @Data
    public static class Probabilities {
        @JsonProperty("standard_symbols")
        private List<StandardSymbolProbability> standardSymbols;
        @JsonProperty("bonus_symbols")
        private BonusSymbolProbability bonusSymbols;
    }

    @Data
    public static class StandardSymbolProbability {
        @JsonProperty("column")
        private int column;
        @JsonProperty("row")
        private int row;
        @JsonProperty("symbols")
        private Map<String, Integer> symbols;
    }

    @Data
    public static class BonusSymbolProbability {
        @JsonProperty("symbols")
        private Map<String, Integer> symbols;
    }

    @Data
    public static class WinCombination {
        @JsonProperty("reward_multiplier")
        private double rewardMultiplier;
        @JsonProperty("when")
        private String when;
        @JsonProperty("count")
        private int count;
        @JsonProperty("group")
        private String group;
        @JsonProperty("covered_areas")
        private List<List<String>> coveredAreas;
    }
}
