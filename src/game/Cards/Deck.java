package game.Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck extends Card {
    private int nrCardsInDeck;
    private List<Card> cards;

    // COnstructor cu ajutorul caruia initializez deck-ul
    public Deck(int mana, int attackDamage, int health, String description, List<String> colors, String name, List<Card> cards) {
        super(mana, attackDamage, health, description, colors, name);
        this.nrCardsInDeck = cards.size();
        this.cards = new ArrayList<>(cards);
    }


    // Alte metode specifice pentru Deck

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }
    public void setNrCardsInDeck(int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }
    public List<Card> getCards() {
        return cards;
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    //Aici vom seta cartea de tip erou al player-ului
    public void addHero(Card card) {
        this.cards.add(card);
    }

    public void shuffleDeck(long shuffleSeed) {
        // Creează o instanță Random cu seed-ul specificat
        Random random = new Random(shuffleSeed);

        // Folosește metoda shuffle din Collections, specificând instanța de Random
        Collections.shuffle(cards, random);
    }
}
