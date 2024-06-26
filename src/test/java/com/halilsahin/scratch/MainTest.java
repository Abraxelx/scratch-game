package com.halilsahin.scratch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    private static ByteArrayOutputStream outContent;
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @BeforeAll
    public static void setUp() {
        // Capture the logger output
        outContent = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outContent);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        CustomConsoleHandler customHandler = new CustomConsoleHandler(outContent);
        LOGGER.addHandler(customHandler);
        LOGGER.setLevel(Level.ALL);
    }

    @Test
    void testMain() {
        String[] args = {"--config", "src/main/resources/config.json", "--betting-amount", "100"};
        Main.main(args);

        String logOutput = outContent.toString();
        assertTrue(logOutput.contains("appliedWinningCombinations"));
        assertTrue(logOutput.contains("appliedBonusSymbol"));
    }

    @Test
    void testSpecificMatrixReward() throws Exception {
        String configPath = "src/main/resources/config.json";
        Config config = Config.load(configPath);

        String[][] testMatrix = {
                {"A", "A", "B"},
                {"A", "+1000", "B"},
                {"A", "A", "B"}
        };

        RewardCalculator calculator = new RewardCalculator(config);
        RewardCalculator.Result result = calculator.calculateReward(testMatrix, 100);

        assertEquals(3600, result.getReward());
        assertEquals("+1000", result.getAppliedBonusSymbol());
        assertTrue(result.getAppliedWinningCombinations().get("A").contains("same_symbol_5_times"));
        assertTrue(result.getAppliedWinningCombinations().get("B").contains("same_symbol_3_times"));
    }
}
