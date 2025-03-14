package org.poo.game.Players;

import org.poo.fileio.CardInput;
import org.poo.game.Cards.Card;
import org.poo.game.Cards.Deck;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public final class Player {
    @Getter
    @Setter
    private int index;
    @Getter
    @Setter
    private ArrayList<Deck> decks;
    @Getter
    private Card hero;
    @Setter
    @Getter
    private ArrayList<Card> deck;
    @Getter
    private ArrayList<Card> hand;
    @Getter
    @Setter
    private int mana;

    private static final int MAX_HEALTH = 30;

    public Player(final ArrayList<Deck> decks) {
        this.decks = decks;
        this.hero = null;
        this.deck = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.mana = 1;
    }

    public Player() {
        this.decks = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.mana = 1;
        this.hero = null;
    }


    /**
     * functie folosita pentru a initializa un jucator
     * @param heroInput este folosit pentru erooul jucatorului
     */
    public void setHero(final CardInput heroInput) {
        this.hero = new Card(heroInput);
        this.getHero().setHealth(MAX_HEALTH);
        this.getHero().setAttackDamage(0);
    }

    public int getHealth() {
        return this.getHero().getHealth();
    }


    public void setHand(final Card card) {
            hand.add(card);

    }


}
