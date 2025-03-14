package org.poo.game.Cards;

import org.poo.fileio.CardInput;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class Card {

    @Getter
    private int mana;
    @Getter
    @Setter
    private int attackDamage;
    @Getter
    @Setter
    private int health;
    @Getter
    private  String description;
    @Getter
    private List<String> colors; // Listă de culori
    @Getter
    private String name;
    @Getter
    @Setter
    private int frozen;
    @Getter
    @Setter
    private boolean played;
    @Getter
    @Setter
    private boolean abilityUsed;

    // Constructor
    public Card(final CardInput cardinput) {
        this.mana = cardinput.getMana();
        this.attackDamage = cardinput.getAttackDamage();
        this.health = cardinput.getHealth();
        this.description = cardinput.getDescription();
        this.colors = cardinput.getColors();
        this.name = cardinput.getName();
        this.frozen = 0;
        this.played = false;
        this.abilityUsed = false;
    }

    public Card(final int mana, final int attackDamage, final int health, final  String description,
                final List<String> colors, final String name) {
        this.mana = mana;
        this.attackDamage = attackDamage;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.frozen = 0;
        this.played = false;
        this.abilityUsed = false;
    }



}
