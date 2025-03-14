package org.poo.game.Change;

import org.poo.fileio.CardInput;
import org.poo.fileio.DecksInput;
import org.poo.game.Cards.Deck;
import org.poo.game.Players.Player;
import org.poo.game.Cards.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Getter
public class GameMaster {
    @Setter
    private Player player1;
    @Setter
    private Player player2;
    @Setter
    private int player1Score;
    @Setter
    private int player2Score;
    @Setter
    private int turn;
    @Setter
    private int shuffleSeed;



    // Constructor
    public GameMaster() {
        this.player1 = new Player();
        this.player2 = new Player();
    }

    // Metode getter pentru a obține deck-urile fiecărui jucător
    // Metode getter pentru a obține deck-urile fiecărui jucător
    public ArrayList<Deck> getPlayer1Decks() {
        return player1.getDecks(); // Castare la ArrayList
    }

    public ArrayList<Deck> getPlayer2Decks() {
        return player2.getDecks(); // Castare la ArrayList
    }


    /**
     * functie care returneaza jucatorul aferent randului din joc
     * @param playerIdx parametru folosit pentru a identifica randul
     * @return va returna jucatorul corespunzator
     */
    public Player getPlayer(final int playerIdx) {
        if (playerIdx == 1) {
            return player1;
        } else if (playerIdx == 2) {
            return player2;
        }
        return null;
    }

    /**
     * functie care schimba randul jcuatorilor
     * @param turn se foloseste pentru a cunoaste randul cui a fost
     *             ultima oara
     */
    public void switchTurn(final int turn) {
        if (turn == 1) {
            this.turn = 2;
        } else {
            this.turn = 1;
        }
    }

    /**
     * functie pentru initializarea deckul primului jucator
     * @param playerOneDecks face rost de deckurile jucatorului
     */

    public void setPlayer1Decks(final DecksInput playerOneDecks) {
        ArrayList<Deck> decks = convertToDecks(playerOneDecks);
        player1.setDecks(decks);
    }

    /**
     * functia aceasta este aceeasi cu cea de sus
     * @param playerTwoDecks furnizeaza deck-ul jucatorului 2
     */

    public void setPlayer2Decks(final DecksInput playerTwoDecks) {
        ArrayList<Deck> decks = convertToDecks(playerTwoDecks);
        player2.setDecks(decks);
    }

    /**
     * am  facut o functie de conversie pentru a imi fi mai usor sa pot
     * pastra deck-urile in clasele mele, din cele in care se face citirea
     * @param decksInput parametru ce ofera deck-urile
     * @return voi avea un Array<List> de deck-uri
     */
    private ArrayList<Deck> convertToDecks(final DecksInput decksInput) {
        ArrayList<Deck> decks = new ArrayList<>();

        for (ArrayList<CardInput> deckInput : decksInput.getDecks()) {
            ArrayList<Card> cards = new ArrayList<>();

            for (CardInput cardInput : deckInput) {
                Card card = new Card(cardInput.getMana(), cardInput.getAttackDamage(),
                        cardInput.getHealth(), cardInput.getDescription(),
                        cardInput.getColors(), cardInput.getName());
                cards.add(card);
            }

            Deck deck = new Deck(0, 0, 0, "",
                    new ArrayList<>(), "", cards);
            decks.add(deck);
        }

        return decks;
    }


    /**
     * functie care imi amesteca deck-ul jucatroului
     * @param playerIndex index-ul jucatorului
     * @param deckIndex indexu-ul deck-ului care este folosit
     * @param seed valoarea random cu care amestecam deck-ul
     */
    public void shuffleDeck(final int playerIndex, final int deckIndex,
                            final long seed) {

        Player player = getPlayer(playerIndex);
        List<Deck> playerDecks = (playerIndex == 1) ? getPlayer1Decks() : getPlayer2Decks();

        if (deckIndex >= 0 && deckIndex < playerDecks.size()) {

            Deck selectedDeck = playerDecks.get(deckIndex);
            List<Card> cardsToShuffle = selectedDeck.getCards();
            Collections.shuffle(cardsToShuffle, new Random(seed));

            if (!cardsToShuffle.isEmpty()) {
                player.setHand(cardsToShuffle.get(0));
                cardsToShuffle.remove(0);
            }

            player.setDeck(new ArrayList<>(cardsToShuffle));
        }
    }
}
