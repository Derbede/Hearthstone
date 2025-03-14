package org.poo.main;

import org.poo.checker.Checker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.*;
import org.poo.game.Cards.Card;
import org.poo.game.Change.Mechanics;
import org.poo.game.Change.GameMaster;
import org.poo.game.Players.Player;
import org.poo.game.Change.PlayerCommand;
import org.poo.game.Players.Error;
import org.poo.game.Change.Actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class Main {
    private Main() {
    }

    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

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

    public static void action(final String filePath1, final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH +
                filePath1), Input.class);

        if (inputData == null) {
            return;
        }

        ArrayNode output = objectMapper.createArrayNode();
        int playerOneScore = 0;
        int playerTwoScore = 0;

        for (GameInput inputGame : inputData.getGames()) {
            GameMaster gameMaster = new GameMaster();
            gameMaster.setPlayer1Decks(inputData.getPlayerOneDecks());
            gameMaster.setPlayer2Decks(inputData.getPlayerTwoDecks());
            gameMaster.setPlayer2Score(playerTwoScore);
            gameMaster.setPlayer1Score(playerOneScore);

            int shuffleSeed = inputGame.getStartGame().getShuffleSeed();
            gameMaster.getPlayer1().setIndex(inputGame.getStartGame().getPlayerOneDeckIdx());
            gameMaster.getPlayer2().setIndex(inputGame.getStartGame().getPlayerTwoDeckIdx());

            gameMaster.shuffleDeck(1, inputGame.getStartGame().getPlayerOneDeckIdx(), shuffleSeed);
            gameMaster.shuffleDeck(2, inputGame.getStartGame().getPlayerTwoDeckIdx(), shuffleSeed);
            gameMaster.getPlayer(1).setHero(inputGame.getStartGame().getPlayerOneHero());
            gameMaster.getPlayer(2).setHero(inputGame.getStartGame().getPlayerTwoHero());
            gameMaster.setTurn(inputGame.getStartGame().getStartingPlayer());

            Mechanics mechanics = new Mechanics(gameMaster);
            Actions actions = new Actions(mechanics);

            for (ActionsInput action : inputGame.getActions()) {
                String actionName = action.getCommand();

                switch (actionName) {
                    case "getPlayerDeck" -> {
                        Player player = gameMaster.getPlayer(action.getPlayerIdx());
                        ObjectNode deckOutput = PlayerCommand.getPlayerDeck(
                                player, action.getPlayerIdx());
                        output.add(deckOutput);
                    }
                    case "getPlayerHero" -> {
                        Player player = gameMaster.getPlayer(action.getPlayerIdx());
                        ObjectNode heroOutput = PlayerCommand.getPlayerHero(
                                player, action.getPlayerIdx());
                        output.add(heroOutput);
                    }
                    case "getPlayerTurn" -> {
                        ObjectNode turnOutput = PlayerCommand.getPlayerTurn(gameMaster.getTurn());
                        output.add(turnOutput);
                    }
                    case "placeCard" -> {
                        Player player = gameMaster.getPlayer(gameMaster.getTurn());
                        if (gameMaster.getPlayer(
                                gameMaster.getTurn()).getHand().get(action.getHandIdx()).getMana()
                                > player.getMana()) {
                            ObjectNode error = Error.noMana(action.getHandIdx());
                            output.add(error);
                        } else {
                            Mechanics.placeCard(player, action.getHandIdx());
                        }
                    }
                    case "getCardsInHand" -> {
                        Player player = gameMaster.getPlayer(action.getPlayerIdx());
                        ObjectNode cards = PlayerCommand.getCardsInHand(
                                player, action.getPlayerIdx());
                        output.add(cards);
                    }
                    case "getPlayerMana" -> {
                        Player player = gameMaster.getPlayer(action.getPlayerIdx());
                        ObjectNode mana = PlayerCommand.getPlayerMana(
                                player, action.getPlayerIdx());
                        output.add(mana);
                    }
                    case "getCardsOnTable" -> {
                        Card[][] cardsOnTable = Mechanics.getCardMatrix();
                        ObjectNode cardsTable = PlayerCommand.getCardsOnTable(cardsOnTable);
                        output.add(cardsTable);
                    }
                    case "cardUsesAttack" -> {
                        int xAttacker = action.getCardAttacker().getX();
                        int yAttacker = action.getCardAttacker().getY();
                        int xAttacked = action.getCardAttacked().getX();
                        int yAttacked = action.getCardAttacked().getY();
                        actions.cardUsed(xAttacker, yAttacker, xAttacked, yAttacked);
                        ObjectNode error = Error.cardAttack(xAttacker, yAttacker,
                                xAttacked, yAttacked, Error.attackCard);
                        if (error != null) {
                            output.add(error);
                            Error.attackCard = 0;
                        }
                    }
                    case "getCardAtPosition" -> {
                        if (Mechanics.getCardMatrix()[action.getX()][action.getY()] == null) {
                            ObjectNode errorPosition =
                                    Error.cardAvailable(action.getX(), action.getY());
                            output.add(errorPosition);
                        } else {
                            ObjectNode card = PlayerCommand.getCardOnTable(
                                    Mechanics.getCardMatrix()[action.getX()][action.getY()],
                                    action.getX(), action.getY());
                            output.add(card);
                        }
                    }
                    case "cardUsesAbility" -> {
                        int xAttacker = action.getCardAttacker().getX();
                        int yAttacker = action.getCardAttacker().getY();
                        int xAttacked = action.getCardAttacked().getX();
                        int yAttacked = action.getCardAttacked().getY();
                        actions.cardUsesAbility(xAttacker, yAttacker, xAttacked, yAttacked);
                        ObjectNode error = Error.cardAbility(xAttacker, yAttacker, xAttacked,
                                yAttacked, Error.cardAbility);
                        if (error != null) {
                            output.add(error);
                            Error.cardAbility = 0;
                        }
                    }
                    case "useAttackHero" -> {
                        int player1Score = gameMaster.getPlayer1Score();
                        int player2Score = gameMaster.getPlayer2Score();
                        actions.attackHero(action.getCardAttacker().getX(),
                                action.getCardAttacker().getY());
                        if (gameMaster.getPlayer1Score() > player1Score) {
                            playerOneScore++;
                            ObjectNode winner = PlayerCommand.gameEnded(1);
                            output.add(winner);
                        } else if (gameMaster.getPlayer2Score() > player2Score) {
                            playerTwoScore++;
                            ObjectNode winner = PlayerCommand.gameEnded(2);
                            output.add(winner);
                        }
                        ObjectNode errorAttackHero = Error.cardAttackHero(
                                action.getCardAttacker().getX(),
                                action.getCardAttacker().getY(), Error.attackCard);
                        if (errorAttackHero != null) {
                            output.add(errorAttackHero);
                            Error.attackCard = 0;
                        }
                    }
                    case "useHeroAbility" -> {
                        actions.useHeroAbility(gameMaster.getTurn(), action.getAffectedRow());
                        ObjectNode errorHero = Error.heroAbility(
                                action.getAffectedRow(), Error.herroAttackError);
                        if (errorHero != null) {
                            output.add(errorHero);
                            Error.herroAttackError = 0;
                        }
                    }
                    case "getFrozenCardsOnTable" -> {
                        ObjectNode frozenCards = PlayerCommand.getFrozenCards(
                                Mechanics.getCardMatrix());
                        output.add(frozenCards);
                    }
                    case "endPlayerTurn" -> {
                        gameMaster.switchTurn(gameMaster.getTurn());
                        Mechanics.switchTurn();
                    }
                    case "getPlayerOneWins" -> {
                        int score = gameMaster.getPlayer1Score();
                            ObjectNode player1Wins = PlayerCommand.getWins(score, 1);
                            output.add(player1Wins);
                    }
                    case "getPlayerTwoWins" -> {
                        int score = gameMaster.getPlayer2Score();
                            ObjectNode player2Wins = PlayerCommand.getWins(score, 2);
                            output.add(player2Wins);
                    }
                    case "getTotalGamesPlayed" -> {
                        int scorePlayer1 = gameMaster.getPlayer1Score();
                        int scorePlayer2 = gameMaster.getPlayer2Score();
                        ObjectNode gamesPlayed = PlayerCommand.getGames(
                                scorePlayer1 + scorePlayer2);
                        output.add(gamesPlayed);
                    }
                    default -> {
                        return;
                    }
                }
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
