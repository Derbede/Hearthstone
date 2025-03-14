package game.Change;

import fileio.CardInput;
import fileio.DecksInput;
import game.Cards.Deck;
import game.Players.Player;
import game.Cards.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public final class Game_master {
    @Getter
    @Setter
    private Player Player1;
    @Getter
    @Setter
    private Player Player2;
    private Card[][] cardsMatrix; // Matrice de carduri
    @Getter
    @Setter
    private int turn;
    @Getter
    @Setter
    private int shuffleSeed;

    // Constructor
    public Game_master() {
        this.Player1 = new Player();
        this.Player2 = new Player();
        this.cardsMatrix = new Card[4][5];
    }

    // Metode getter pentru a obține deck-urile fiecărui jucător
    // Metode getter pentru a obține deck-urile fiecărui jucător
    public ArrayList<Deck> getPlayer1Decks() {
        return (ArrayList<Deck>) Player1.getDecks(); // Castare la ArrayList
    }

    public ArrayList<Deck> getPlayer2Decks() {
        return (ArrayList<Deck>) Player2.getDecks(); // Castare la ArrayList
    }


    public Player getPlayer(int playerIdx) {
        if (playerIdx == 1) {
            return Player1;
        } else if (playerIdx == 2) {
            return Player2;
        }
        return null; // Sau aruncă o excepție dacă indexul este invalid
    }

    public int setShuffleSeed(int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
        return shuffleSeed;
    }

    public int setTurn(int turn) {
        this.turn = turn;
        return turn;
    }

    public void setPlayer1Decks(DecksInput playerOneDecks) {
        ArrayList<Deck> decks = convertToDecks(playerOneDecks);
        Player1.setDecks(decks);
    }

    public void setPlayer2Decks(DecksInput playerTwoDecks) {
        ArrayList<Deck> decks = convertToDecks(playerTwoDecks);
        Player2.setDecks(decks);
    }

    // Metodă privată pentru a transforma DecksInput în Deck-uri
    private ArrayList<Deck> convertToDecks(DecksInput decksInput) {
        ArrayList<Deck> decks = new ArrayList<>();

        // Presupunem că pentru fiecare deck avem o listă de carduri
        for (ArrayList<CardInput> deckInput : decksInput.getDecks()) {
            ArrayList<Card> cards = new ArrayList<>();

            // Convertim fiecare CardInput în Card
            for (CardInput cardInput : deckInput) {
                Card card = new Card(cardInput.getMana(), cardInput.getAttackDamage(),
                        cardInput.getHealth(), cardInput.getDescription(),
                        cardInput.getColors(), cardInput.getName());
                cards.add(card);
            }

            // Presupunem că folosim parametrii default pentru Deck
            Deck deck = new Deck(0, 0, 0, "", new ArrayList<>(), "", cards);
            decks.add(deck);
        }

        return decks;
    }



}
