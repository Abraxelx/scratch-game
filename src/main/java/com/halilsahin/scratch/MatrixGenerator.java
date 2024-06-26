package com.halilsahin.scratch;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class generates a matrix with standard and bonus symbols based on the provided configuration.
 *
 * @author Halil Şahin
 */
public class MatrixGenerator {

    private final Config config;
    private final Random rand;

    public MatrixGenerator(Config config) {
        this.config = config;
        this.rand = new Random(); // Reuse the Random object
    }

    /**
     * Generates a matrix based on the configuration provided.
     *
     * @return a 2D array representing the generated matrix with standard and bonus symbols.
     */
    public String[][] generateMatrix() {
        String[][] matrix = new String[config.getRows()][config.getColumns()];

        placeStandardSymbols(matrix);
        placeBonusSymbols(matrix);
        fillNullCells(matrix);

        return matrix;
    }

    /**
     * Places standard symbols in the matrix based on their probabilities.
     *
     * @param matrix the matrix to fill with standard symbols.
     */
    private void placeStandardSymbols(String[][] matrix) {
        for (Config.StandardSymbolProbability prob : config.getProbabilities().getStandardSymbols()) {
            int row = prob.getRow();
            int column = prob.getColumn();

            List<String> symbols = createShuffledSymbolList(prob);
            fillMatrixWithSymbols(matrix, row, column, symbols);
        }
    }

    /**
     * Creates a shuffled list of symbols based on their probabilities.
     *
     * @param prob the probability configuration for the symbols.
     * @return a shuffled list of symbols.
     */
    private List<String> createShuffledSymbolList(Config.StandardSymbolProbability prob) {
        List<String> symbols = prob.getSymbols().entrySet().stream()
                .flatMap(entry -> Collections.nCopies(entry.getValue(), entry.getKey()).stream())
                .collect(Collectors.toList());
        Collections.shuffle(symbols, rand);
        return symbols;
    }

    /**
     * Fills the matrix with symbols from the given list at specified positions.
     *
     * @param matrix  the matrix to fill with symbols.
     * @param row     the row to start filling from.
     * @param column  the column to start filling from.
     * @param symbols the list of symbols to fill the matrix with.
     */
    private void fillMatrixWithSymbols(String[][] matrix, int row, int column, List<String> symbols) {
        for (int r = 0; r < config.getRows(); r++) {
            for (int c = 0; c < config.getColumns(); c++) {
                if (matrix[r][c] == null && r == row && c == column) {
                    Iterator<String> iterator = symbols.iterator();
                    if (iterator.hasNext()) {
                        String symbol = iterator.next();
                        iterator.remove();
                        matrix[r][c] = symbol;
                    }
                }
            }
        }
    }

    /**
     * Places a bonus symbol in the matrix based on their probabilities.
     *
     * @param matrix the matrix to fill with a bonus symbol.
     */
    private void placeBonusSymbols(String[][] matrix) {
        int totalWeight = config.getProbabilities().getBonusSymbols().getSymbols().values().stream().mapToInt(Integer::intValue).sum();
        int randomCell = rand.nextInt(config.getRows() * config.getColumns());

        int bonusIndex = rand.nextInt(totalWeight);
        int cumulativeWeight = 0;
        String selectedBonus = null;
        for (Map.Entry<String, Integer> entry : config.getProbabilities().getBonusSymbols().getSymbols().entrySet()) {
            cumulativeWeight += entry.getValue();
            if (bonusIndex < cumulativeWeight) {
                selectedBonus = entry.getKey();
                break;
            }
        }

        if (selectedBonus != null) {
            int row = randomCell / config.getColumns();
            int column = randomCell % config.getColumns();
            matrix[row][column] = selectedBonus;
        }
    }

    /**
     * Fills any remaining null cells in the matrix with random standard symbols.
     *
     * @param matrix the matrix to fill.
     */
    private void fillNullCells(String[][] matrix) {
        List<String> allSymbols = config.getSymbols().keySet().stream()
                .filter(sym -> config.getSymbols().get(sym).getType().equals("standard"))
                .collect(Collectors.toList());

        for (int r = 0; r < config.getRows(); r++) {
            for (int c = 0; c < config.getColumns(); c++) {
                if (matrix[r][c] == null) {
                    matrix[r][c] = allSymbols.get(rand.nextInt(allSymbols.size()));
                }
            }
        }
    }
}
