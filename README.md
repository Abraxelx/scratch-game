# Scratch Game

This is a scratch game project developed in Java. It generates a game matrix, calculates rewards based on the symbols in the matrix, and applies any bonus symbols present.

## Prerequisites

- Java 11 or higher
- Maven


## Project Structure
- src/main/java/com/halilsahin/scratch/: Contains the main application code.
- src/main/resources/: Contains the configuration files.
- src/test/java/com/halilsahin/scratch/: Contains the unit tests.


## Building the Project

To build the project, run the following command in the project root directory:

```sh
mvn clean package
```



This will create two JAR files in the target directory:

1. scratch-game-1.0.jar
2. scratch-game-1.0-jar-with-dependencies.jar
Use the scratch-game-1.0-jar-with-dependencies.jar to run the application.

## Running the Application
To run the application, use the following command:

```sh
java -jar target/scratch-game-1.0-jar-with-dependencies.jar --config src/main/resources/config.json --betting-amount 100
```

## Configuration
The configuration file (config.json) defines the symbols, their probabilities, and the winning combinations. You can modify this file to change the game's behavior.
The configuration file is located at src/main/resources/config.json. You can modify this file to change the game settings, symbols, probabilities, and winning combinations.

## Example Output
The application will print the game matrix, the calculated reward, the applied winning combinations, and the applied bonus symbol in JSON format.

```json
{
    "matrix": [
        ["A", "A", "B"],
        ["A", "+1000", "B"],
        ["A", "A", "B"]
    ],
    "reward": 6600,
    "applied_winning_combinations": {
        "A": ["same_symbol_5_times", "same_symbols_vertically"],
        "B": ["same_symbol_3_times", "same_symbols_vertically"]
    },
    "applied_bonus_symbol": "+1000"
}
```

