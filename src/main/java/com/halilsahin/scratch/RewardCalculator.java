package com.halilsahin.scratch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class calculates the reward based on the generated matrix and the provided configuration.
 *
 * author Halil Şahin
 */
public class RewardCalculator {
    private final Config config;
    private static final Logger LOGGER = Logger.getLogger(RewardCalculator.class.getName());

    public RewardCalculator(Config config) {
        this.config = config;
    }

    /**
     * Calculates the reward based on the given matrix and betting amount.
     *
     * @param matrix the generated matrix
     * @param betAmount the betting amount
     * @return the result containing the matrix, total reward, applied winning combinations, and applied bonus symbol
     */
    public Result calculateReward(String[][] matrix, int betAmount) {
        int totalReward = 0;
        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
        String appliedBonusSymbol = null;

        // Calculate rewards for standard symbols
        for (String symbol : config.getSymbols().keySet()) {
            if (config.getSymbols().get(symbol).getType().equals("standard")) {
                int symbolReward = 0;
                int symbolCount = 0;

                // Find the count of the symbol in the matrix
                for (int r = 0; r < config.getRows(); r++) {
                    for (int c = 0; c < config.getColumns(); c++) {
                        if (matrix[r][c].equals(symbol)) {
                            symbolCount++;
                        }
                    }
                }

                if (symbolCount > 0) {
                    // Find the winning combination for the symbol
                    List<String> winningCombinations = new ArrayList<>();
                    double maxCountMultiplier = 0;
                    double maxLinearMultiplier = 0;

                    String maxCountCombination = null;
                    String maxLinearCombination = null;

                    for (String combinationKey : config.getWinCombinations().keySet()) {
                        Config.WinCombination combination = config.getWinCombinations().get(combinationKey);

                        // Check for same symbols combination
                        if (combination.getWhen().equals("same_symbols") && combination.getCount() <= symbolCount) {
                            if (combination.getRewardMultiplier() > maxCountMultiplier) {
                                maxCountMultiplier = combination.getRewardMultiplier();
                                maxCountCombination = combinationKey;
                            }
                        }

                        // Check for linear symbols combination
                        if (combination.getWhen().equals("linear_symbols")) {
                            for (List<String> area : combination.getCoveredAreas()) {
                                if (checkCoveredArea(matrix, symbol, area)) {
                                    if (combination.getRewardMultiplier() > maxLinearMultiplier) {
                                        maxLinearMultiplier = combination.getRewardMultiplier();
                                        maxLinearCombination = combinationKey;
                                    }
                                }
                            }
                        }
                    }

                    if (maxCountCombination != null) {
                        winningCombinations.add(maxCountCombination);
                    }
                    if (maxLinearCombination != null) {
                        winningCombinations.add(maxLinearCombination);
                    }

                    if (!winningCombinations.isEmpty()) {
                        symbolReward = (int) Math.round(betAmount * config.getSymbols().get(symbol).getRewardMultiplier()
                                * maxCountMultiplier * Math.max(1, maxLinearMultiplier));
                        appliedWinningCombinations.put(symbol, winningCombinations);
                    }
                    totalReward += symbolReward;
                }
            }
        }

        // Apply bonus symbols
        for (int r = 0; r < config.getRows(); r++) {
            for (int c = 0; c < config.getColumns(); c++) {
                String cell = matrix[r][c];
                if (config.getSymbols().get(cell).getType().equals("bonus")) {
                    appliedBonusSymbol = cell;
                    Config.Symbol bonusSymbol = config.getSymbols().get(cell);
                    switch (bonusSymbol.getImpact()) {
                        case "multiply_reward":
                            totalReward *= bonusSymbol.getRewardMultiplier();
                            break;
                        case "extra_bonus":
                            totalReward += bonusSymbol.getExtra();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        // If no winning combinations, set reward to 0 and mark as LOSS
        if (appliedWinningCombinations.isEmpty()) {
            totalReward = 0;
            appliedBonusSymbol = "LOSS";
        }

        return new Result(matrix, totalReward, appliedWinningCombinations, appliedBonusSymbol);
    }

    /**
     * Checks if the specified area in the matrix contains the given symbol.
     *
     * @param matrix the matrix to check
     * @param symbol the symbol to look for
     * @param area the area to check
     * @return true if the area contains the symbol, false otherwise
     */
    private boolean checkCoveredArea(String[][] matrix, String symbol, List<String> area) {
        for (String pos : area) {
            String[] parts = pos.split(":");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            if (!matrix[row][col].equals(symbol)) {
                return false;
            }
        }
        return true;
    }

    @Data
    @AllArgsConstructor
    public static class Result {
        private String[][] matrix;
        private int reward;
        private Map<String, List<String>> appliedWinningCombinations;
        private String appliedBonusSymbol;

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "Failed to convert result to JSON: " + e.getMessage(), e);
                return super.toString();
            }
        }
    }
}
