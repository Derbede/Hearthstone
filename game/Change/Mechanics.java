package org.poo.game.Change;

import org.poo.game.Cards.Card;
import org.poo.game.Players.Player;
import lombok.Getter;
import lombok.Setter;

public class Mechanics extends GameMaster {

    static GameMaster game;

    private static final int MAX_COLUMNS = 5;
    private static final int MAX_ROWS = 4;
    private static final int MAX_TURNS = 10;

    @Getter
    @Setter
    private static Card[][] cardMatrix;
    @Getter
    @Setter
    private static boolean turnPlayer1;
    @Getter
    @Setter
    private static boolean turnPlayer2;

    private static int turns;


    public Mechanics(final GameMaster game) {
        Mechanics.game = game;
        cardMatrix = new Card[MAX_ROWS][MAX_COLUMNS];
        if (Mechanics.game.getTurn() == 1) {
            setTurnPlayer1(true);
            setTurnPlayer2(false);
        } else {
            setTurnPlayer1(false);
            setTurnPlayer2(true);
        }
        turns = 2;

    }

    /**
     * schimba tura jucatorilor, adauga mana fiecarui jucator, le seteaza abilitatile eroilor
     * ca fiind nefolosite, si a tuturor cartilor si se modifica si daca sunt inghetate
     * sau nu. Am pus partea de turns la 2, deoarece asa am gandit sa se actualizeze mana
     * jucatorilor, dupa 2 ture, practic trece o runda si se adauga fix cat este nevoie,
     * dar am facut astfel incat sa nu treaca de limita de 10 mana pe runda.
     */

    public static void switchTurn() {
        turnPlayer1 = !turnPlayer1;
        turnPlayer2 = !turnPlayer2;
        turns++;
        game.getPlayer1().getHero().setAbilityUsed(false);
        game.getPlayer2().getHero().setAbilityUsed(false);

        if (turns % 2 == 0) {
            game.getPlayer2().setMana(game.getPlayer2().getMana() + Math.min(turns / 2, MAX_TURNS));
            game.getPlayer1().setMana(game.getPlayer1().getMana() + Math.min(turns / 2, MAX_TURNS));
            if (!game.getPlayer1().getDeck().isEmpty()) {
                game.getPlayer1().getHand().add(game.getPlayer1().getDeck().get(0));
                game.getPlayer1().getDeck().remove(0);
            }
            if (!game.getPlayer2().getDeck().isEmpty()) {
                game.getPlayer2().getHand().add(game.getPlayer2().getDeck().get(0));
                game.getPlayer2().getDeck().remove(0);
            }
        }

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (cardMatrix[i][j] != null) {
                    cardMatrix[i][j].setPlayed(false);
                    cardMatrix[i][j].setAbilityUsed(false);
                    cardMatrix[i][j].setFrozen(cardMatrix[i][j].getFrozen() - 1);
                }
            }
        }

    }

    /**
     * aceasta functie plaseaza cartile pe tabla de joc
     * @param player pune cartea din mana jucatorului curent
     * @param index reprezinta a cata carte din mana trebuie plasata
     * @return modifica tabla de joc
     */


    public static Card[][] placeCard(final Player player, final int index) {
        // Verifică dacă mâna jucătorului nu este goală și indexul este valid
        if (player.getHand().isEmpty() || index < 0 || index >= player.getHand().size()) {
            return cardMatrix;
        }

        // Verifică dacă jucătorul are suficientă mană pentru a plasa cartea
        Card selectedCard = player.getHand().get(index);

        // Avem nevoie de linia pe care să plasăm cărțile
        int row;
        if (selectedCard.getName().equals("Goliath") || selectedCard.getName().equals("Warden")
            || selectedCard.getName().equals("Miraj")
                || selectedCard.getName().equals("The Ripper")) {
            row = turnPlayer1 ? 2 : 1; // Linia 2 pentru player1, linia 1 pentru player2
        } else {
            row = turnPlayer1 ? MAX_ROWS - 1 : 0; // Linia 0 pentru player1, linia 3 pentru player2
        }

        // Plasează cartea în prima poziție disponibilă din linia determinată
        // Plasează cartea în prima poziție disponibilă din linia determinată
        for (int i = 0; i < cardMatrix[row].length; i++) {
            if (cardMatrix[row][i] == null) {
                cardMatrix[row][i] = selectedCard;
                player.setMana(player.getMana() - selectedCard.getMana());

                // Elimină cartea din mână la indexul specificat
                player.getHand().remove(index);

                break;
            }
        }
        return cardMatrix;
    }
/**
    public static void printCardTable(Card[][] matrix) {
        System.out.println("Current Card Table:");
        for (int row = 0; row < matrix.length; row++) {
            System.out.print("Row " + row + ": ");
            for (int col = 0; col < matrix[row].length; col++) {
                Card card = matrix[row][col];
                if (card != null) {
                    System.out.print("[" + card.getName() +
                            " (Mana: " + card.getMana() +
                            ", Health: " + card.getHealth() +
                            ", Attack: " + card.getAttackDamage() +
                            ")] ");
                } else {
                    System.out.print("[Empty] ");
                }
            }
            System.out.println(); // Trecem la linia următoare
        }
        System.out.println(); // Linie goală după afisarea tablei
    }

**/
}
