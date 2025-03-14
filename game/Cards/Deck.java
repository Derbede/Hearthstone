package org.poo.game.Cards;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public final class Deck extends Card {
    @Setter
    private int nrCardsInDeck;
    private List<Card> cards;

    // COnstructor cu ajutorul caruia initializez deck-ul
    public Deck(final int mana, final int attackDamage, final int health, final String description,
                final List<String> colors, final String name, final List<Card> cards) {
        super(mana, attackDamage, health, description, colors, name);
        this.nrCardsInDeck = cards.size();
        this.cards = new ArrayList<>(cards);
    }

    public Deck() {
        super(0, 0, 0, "", new ArrayList<>(), ""); // Inițializare cu valori default
        this.cards = new ArrayList<>();
    }

    // Alte metode specifice pentru Deck

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public List<Card> getCards() {
        return cards;
    }
}
