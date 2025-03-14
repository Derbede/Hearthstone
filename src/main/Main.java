package main;

import checker.Checker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import checker.CheckerConstants;
import fileio.*;
import game.Cards.Deck;
import game.Change.Game_master;
import game.Players.Player;
import game.Change.PlayerCommand;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        // Verificăm dacă directorul de rezultate există, dacă nu, îl creăm
        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }


    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1, final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Citește fișierul JSON complet
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1), Input.class);
        GameInput inputGame = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1), GameInput.class);

        // Verificare pentru secțiunea `startGame`
        if (inputGame.getStartGame() == null) {
            System.err.println("Eroare: Secțiunea 'startGame' lipsește din fișierul JSON de input. "
                    + "Asigură-te că fișierul are structura corectă.");
            // Opțional: Poți opri execuția aici dacă `startGame` este necesar
            return;
        }

        // Creează instanțe ale jucătorilor și jocului
        Game_master game_master = new Game_master();
        game_master.setPlayer1Decks(inputData.getPlayerOneDecks());
        game_master.setPlayer2Decks(inputData.getPlayerTwoDecks());

        // Inițializează datele jocului folosind `startGame`
        game_master.getPlayer(1).setIndex(inputGame.getStartGame().getPlayerOneDeckIdx());
        game_master.getPlayer(2).setIndex(inputGame.getStartGame().getPlayerTwoDeckIdx());
        game_master.setShuffleSeed(inputGame.getStartGame().getShuffleSeed());
        game_master.getPlayer(1).setHero(inputGame.getStartGame().getPlayerOneHero());
        game_master.getPlayer(2).setHero(inputGame.getStartGame().getPlayerTwoHero());
        game_master.setTurn(inputGame.getStartGame().getStartingPlayer());

        // Creează nodul pentru output
        ArrayNode output = objectMapper.createArrayNode();

        // Procesează acțiunile
        for (ActionsInput action : inputGame.getActions()) {
            String actionName = action.getCommand();

            switch (actionName) {
                case "getPlayerDeck" -> {
                    Player player = game_master.getPlayer(action.getPlayerIdx());
                    ObjectNode deckOutput = PlayerCommand.getPlayerDeck(player, action.getPlayerIdx());
                    output.add(deckOutput);
                }
                case "getPlayerHero" -> {
                    Player player = game_master.getPlayer(action.getPlayerIdx());
                    ObjectNode heroOutput = PlayerCommand.getPlayerHero(player, action.getPlayerIdx());
                    output.add(heroOutput);
                }
                // Adaugă alte acțiuni aici după necesități
            }
        }

        // Scrie rezultatele în fișierul de output
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }


}
