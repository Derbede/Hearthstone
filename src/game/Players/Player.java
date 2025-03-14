package game.Players;

import fileio.CardInput;
import game.Cards.Card;
import game.Cards.Deck;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Player {
    @Getter
    @Setter
    private int index; // să vedem ce deck folosim

    private String name;
    private int score;

    // Lista de deck-uri a jucătorului
    @Getter
    @Setter
    private ArrayList<Deck> decks;

    @Getter
    private Card hero;

    private Deck deck;

    // Constructor cu parametrii
    public Player(String name, int score, ArrayList<Deck> decks) {
        this.name = name;
        this.score = score;
        this.decks = decks;
    }

    // Constructor implicit
    public Player() {
        this.name = "Default Name"; // nume implicit
        this.score = 0; // scor implicit
        this.decks = new ArrayList<>(); // inițializăm lista de deck-uri
    }

    // Metodă pentru setarea eroului
    public void setHero(final CardInput heroInput) {
        this.hero = new Card(heroInput);
    }

    public Card getHero() {
        return this.hero;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public Deck getDeck() {
        return deck;
    }

//    public void setDeck(ArrayList<CardInput> deck) {
//        if (index >= 0 && index < decks.size()) {
//            decks.set(index, deck);
//        } else {
//            System.out.println("Indexul este în afara limitelor.");
//        }
//    }

}
