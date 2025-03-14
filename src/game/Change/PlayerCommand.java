package game.Change;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import game.Cards.Card;
import game.Cards.Deck;
import game.Players.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The type PlayerCommandHandler.
 */
public final class PlayerCommand {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private PlayerCommand() {
    }

    /**
     * Get all player data output as an array.
     *
     * @param players the list of players
     * @param currentPlayerIdx the current player index
     * @return the array of object nodes
     */
    public static List<ObjectNode> getAllPlayerData(Player player1, Player player2, int currentPlayerIdx) {
        List<ObjectNode> outputList = new ArrayList<>();
    
        outputList.add(getPlayerDeck(player1, 1));
        outputList.add(getPlayerDeck(player2, 2));
        outputList.add(getPlayerHero(player1, 1));
        outputList.add(getPlayerHero(player2, 2));
        outputList.add(getPlayerTurn(currentPlayerIdx));

        return outputList;
    }

    /**
     * Get player deck output.
     *
     * @param player the player
     * @param playerIdx the player index
     * @return the object node
     */
    public static ObjectNode getPlayerDeck(Player player, int playerIndex) {
        ObjectNode deckOutput = JsonNodeFactory.instance.objectNode();
        deckOutput.put("command", "getPlayerDeck");
        deckOutput.put("playerIdx", playerIndex);

        if (player != null && player.getDecks() != null && !player.getDecks().isEmpty()) {
            int deckIndex = player.getIndex(); // indexul deck-ului setat în Player
            if (deckIndex < player.getDecks().size()) {
                Deck playerDeck = player.getDecks().get(deckIndex);
                List<Card> cards = playerDeck.getCards(); // Modifică aceasta dacă metoda e diferită

                // Adăugăm fiecare card în array-ul de ieșire
                deckOutput.set("output", objectMapper.valueToTree(cards)); // Aici asigurăm că ieșirea este un array de carduri
            } else {
                deckOutput.put("result", "Deck index is out of bounds.");
            }
        } else {
            deckOutput.put("result", "Player has no decks available.");
        }
        return deckOutput;
    }

    /**
     * Get player hero output.
     *
     * @param player the player
     * @param playerIdx the player index
     * @return the object node
     */
    public static ObjectNode getPlayerHero(Player player, int playerIdx) {
        Card heroCard = player.getHero();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerHero");
        objectNode.put("playerIdx", playerIdx);
        objectNode.set("output", objectMapper.valueToTree(heroCard));

        return objectNode;
    }

    /**
     * Get player turn output.
     *
     * @param currentPlayerIdx the current player index
     * @return the object node
     */
    public static ObjectNode getPlayerTurn(int currentPlayerIdx) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", currentPlayerIdx);

        return objectNode;
    }
}
