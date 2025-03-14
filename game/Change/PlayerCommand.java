package org.poo.game.Change;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.game.Cards.Card;
import org.poo.game.Players.Player;
import java.util.List;


public final class PlayerCommand {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private PlayerCommand() {
    }

    /**
     * functie care scrie in fisierul JSON ce deck are fiecare player
     * @param player este folosit pentru a face rost de deckul playerului aferent
     * @param playerIdx se folosest pentru a lua indexyl deck-ului
     * @return returneaza deck-ul pe care il va folosi playreul
     */

    public static ObjectNode getPlayerDeck(final Player player, final int playerIdx) {
        List<Card> deckCards = player.getDeck();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerDeck");
        objectNode.put("playerIdx", playerIdx);

        ArrayNode outputArray = objectMapper.createArrayNode();

        for (Card card : deckCards) {
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());
            cardNode.set("colors", objectMapper.valueToTree(card.getColors()));
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("name", card.getName());

            outputArray.add(cardNode);
        }

        objectNode.set("output", outputArray);

        return objectNode;
    }

    /**
     * functie care face rost de eroul jucatorului aferent
     * @param player parametru folosit pentru a lua eroul jucatorului
     * @param playerIdx indexul jucatorului de la care trebuie luat eroul
     * @return scrie in fisier mesajul aferent
     */
    public static ObjectNode getPlayerHero(final Player player, final int playerIdx) {
        Card heroCard = player.getHero();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerHero");
        objectNode.put("playerIdx", playerIdx);

        // Creăm un obiect JSON personalizat pentru erou
        ObjectNode heroNode = objectMapper.createObjectNode();
        heroNode.put("mana", heroCard.getMana());
        heroNode.put("health", heroCard.getHealth());
        heroNode.put("description", heroCard.getDescription());
        heroNode.set("colors", objectMapper.valueToTree(heroCard.getColors()));
        heroNode.put("name", heroCard.getName());

        objectNode.set("output", heroNode);

        return objectNode;
    }

    /**
     * functie care afiseaza randul carui jucator este
     * @param currentPlayerIdx tura jucatorului
     * @return afiseaza mesajul aferent
     */

    public static ObjectNode getPlayerTurn(final int currentPlayerIdx) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", currentPlayerIdx);

        return objectNode;
    }

    /**
     * functie care returneaza cartile din mana jucatorului
     * @param player este folosit pentru a sti jucatorul la care facem referire
     * @param playerIdx este folosit pentru a lua cartea de la un anumit index
     * @return afiseaza mesajul respectiv in fisierul JSON
     */

    public static ObjectNode getCardsInHand(final Player player, final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper(); // Asigură-te că ai creat un ObjectMapper
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getCardsInHand");
        objectNode.put("playerIdx", playerIdx);

        // Creează un array pentru cărțile din mână
        ArrayNode outputArray = objectMapper.createArrayNode();

        // Iterează prin cărțile din mână
        for (Card card : player.getHand()) {
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());

            // Creează un array pentru culori
            ArrayNode colorsArray = objectMapper.createArrayNode();
            for (String color : card.getColors()) {
                colorsArray.add(color);
            }

            // Adaugă culorile în cardNode
            cardNode.set("colors", colorsArray);
            cardNode.put("name", card.getName());

            // Adaugă cartea în array-ul de output
            outputArray.add(cardNode);
        }

        // Adaugă array-ul de cărți în objectNode
        objectNode.set("output", outputArray);

        return objectNode;
    }

    /**
     * afiseaza in fisier cata mana are un jucator
     * @param player este folosit pentru a sti mana carui jucator trebuie resturnata
     * @param playerIdx indexul jucatorului la are facem referire
     * @return functia afiseaza nivelul de mana
     */

    public static ObjectNode getPlayerMana(final Player player, final int playerIdx) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerMana");
        objectNode.put("output", player.getMana());
        objectNode.put("playerIdx", playerIdx);
        return objectNode;
    }

    /**
     * functie care ne arata ce carti se afla pe tabla de joc
     * @param matrix este tabla de unde luam cartile
     * @return vom afisa pe linii, de la stanga la dreapta, de
     * sus in jos, fiecare carte care se afla pe tabla
     */

    public static ObjectNode getCardsOnTable(final Card[][] matrix) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getCardsOnTable");
        ArrayNode outputArray = objectMapper.createArrayNode();

        // Parcurgem fiecare linie a matricei
        for (Card[] row : matrix) {
            ArrayNode rowArray = objectMapper.createArrayNode(); // Array pentru fiecare linie

            for (Card card : row) {
                // Verificăm dacă cartea nu este null
                if (card != null) {
                    ObjectNode cardNode = objectMapper.createObjectNode();

                    cardNode.put("attackDamage", card.getAttackDamage());
                    cardNode.put("mana", card.getMana());
                    cardNode.put("health", card.getHealth());
                    cardNode.put("description", card.getDescription());

                    // Adăugăm culorile cărții
                    ArrayNode colorsArray = objectMapper.createArrayNode();
                    for (String color : card.getColors()) {
                        colorsArray.add(color);
                    }
                    cardNode.set("colors", colorsArray);
                    cardNode.put("name", card.getName());

                    // Adăugăm nodul cărții la array-ul liniei curente
                    rowArray.add(cardNode);
                } else {
                    System.out.println("Card is null in matrix.");
                }
            }

            // Adăugăm array-ul liniei curente la array-ul de ieșire
            outputArray.add(rowArray);
        }

        objectNode.set("output", outputArray);
        return objectNode;
    }

    /**
     * la fel ca functia de mai sus, dar afiseaza o singura carte
     * @param card detaliile cartii la care facem referire
     * @param x linia pe care se afla cartea
     * @param y coloana la care se afla cartea
     * @return afiseaza pozitia cartii si caracteristicile acesteia
     */

    public static ObjectNode getCardOnTable(final Card card, final int x,
                                            final int y) {

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getCardAtPosition");

        // Creăm un obiect JSON personalizat pentru erou
        ObjectNode tableCard = objectMapper.createObjectNode();
        tableCard.put("attackDamage", card.getAttackDamage());
        tableCard.put("mana", card.getMana());
        tableCard.put("health", card.getHealth());
        tableCard.put("description", card.getDescription());
        tableCard.set("colors", objectMapper.valueToTree(card.getColors()));
        tableCard.put("name", card.getName());

        objectNode.set("output", tableCard);
        objectNode.put("x", x);
        objectNode.put("y", y);

        return objectNode;
    }

    /**
     * afiseaza momentul in care un jucator castiga
     * @param winner afieaza care jucator a castigat, in functie de valoare
     * @return returneaza mesajul aferent
     */

    public static ObjectNode gameEnded(final int winner) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (winner == 1) {
            objectNode.put("gameEnded", "Player one killed the enemy hero.");
        } else if (winner == 2) {
            objectNode.put("gameEnded", "Player two killed the enemy hero.");
        }
        return objectNode;
    }

    /**
     * functie care afiseaza doar cartile de tip frozen de pe tabla
     * @param matrix tabla de unde luam cartile
     * @return scrie mesajul in fisierul de tip JSON
     */

    public static ObjectNode getFrozenCards(final Card[][] matrix) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getFrozenCardsOnTable");
        ArrayNode outputArray = objectMapper.createArrayNode();

        // Iterăm prin matrice pentru a găsi cărțile înghețate
        for (Card[] row : matrix) {
            for (Card card : row) {
                if (card != null && card.getFrozen() > 0) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("attackDamage", card.getAttackDamage());
                    cardNode.put("mana", card.getMana());
                    cardNode.put("health", card.getHealth());
                    cardNode.put("description", card.getDescription());

                    ArrayNode colorsArray = objectMapper.createArrayNode();
                    for (String color : card.getColors()) {
                        colorsArray.add(color);
                    }

                    cardNode.set("colors", colorsArray);
                    cardNode.put("name", card.getName());

                    // Adăugăm cartea înghețată direct în outputArray
                    outputArray.add(cardNode);
                }
            }
        }

        // Adăugăm outputArray direct la obiectul principal
        objectNode.set("output", outputArray);
        return objectNode;
    }

    /**
     * functie care afiseaza cate meciuri a castigat un jucator
     * @param score reprezinta scorul unui jucator
     * @param wins reprezinta scorul al carui jucator trebuie afisat
     * @return scrie mesajul in fisierul JSON
     */

    public static ObjectNode getWins(final int score, final int wins) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        switch (wins) {
            case 1: objectNode.put("command", "getPlayerOneWins");
            break;
            case 2: objectNode.put("command", "getPlayerTwoWins");
            break;
            default: break;
        }
        objectNode.put("output", score);
        return objectNode;
    }

    /**
     * functie care afiseaza numaruk total de meciuri jcuate
     * @param wins folosim numaru de castiguri total pentru a intoarce
     *             numarul de jocuri jucate de cei 2 playeri
     * @return va afisa mesajul intr-un fisier JSON
     */

    public static ObjectNode getGames(final int wins) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getTotalGamesPlayed");
        objectNode.put("output", wins);
        return objectNode;
    }
}
