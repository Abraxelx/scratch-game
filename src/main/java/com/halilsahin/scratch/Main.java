package com.halilsahin.scratch;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    // Create a Logger
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Options options = new Options();

        Option configFileOption = new Option("c", "config", true, "config file path");
        configFileOption.setRequired(true);
        options.addOption(configFileOption);

        Option betAmountOption = new Option("b", "betting-amount", true, "betting amount");
        betAmountOption.setRequired(true);
        options.addOption(betAmountOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Parsing command line options failed: " + e.getMessage(), e);
            formatter.printHelp("utility-name", options);
            System.exit(1);
            return;
        }

        String configPath = cmd.getOptionValue("config");
        int bettingAmount = Integer.parseInt(cmd.getOptionValue("betting-amount"));

        try {
            Config config = Config.load(configPath);

            MatrixGenerator generator = new MatrixGenerator(config);
            String[][] matrix = generator.generateMatrix();

            RewardCalculator calculator = new RewardCalculator(config);
            RewardCalculator.Result result = calculator.calculateReward(matrix, bettingAmount);

            LOGGER.info(result.toString());

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load configuration: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid betting amount: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred: " + e.getMessage(), e);
        }
    }
}
