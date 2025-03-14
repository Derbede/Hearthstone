package game.Cards;

import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class Card {
    @Getter
    private int mana;
    @Getter
    private int attackDamage;
    @Getter
    @Setter
    private int health;
    @Getter
    private String description;
    @Getter
    private List<String> colors; // Listă de culori
    @Getter
    private String name;
    private boolean frozen;
    private int x;
    private int y;

    // Constructor
    public Card(CardInput cardinput) {
        this.mana = cardinput.getMana();
        this.attackDamage = cardinput.getAttackDamage();
        this.health = cardinput.getHealth();
        this.description = cardinput.getDescription();
        this.colors = cardinput.getColors();
        this.name = cardinput.getName();
    }

    public Card(int mana, int attackDamage, int health, String description, List<String> colors, String name) {
        this.mana = mana;
        this.attackDamage = attackDamage;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }



}
