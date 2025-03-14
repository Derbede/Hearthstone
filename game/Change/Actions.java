package org.poo.game.Change;

import org.poo.game.Cards.Card;
import org.poo.game.Players.Error;
import org.poo.game.Players.Player;

public final class Actions {
    private static final int FROZEN = 1;
    private static final int TANK = 2;
    private static final int ATTACK = 3;
    private static final int NOT_ENEMY_CARD = 4;
    private static final int NOT_ALLY_CARD = 5;
    private static final int MANA = 6;
    private static final int MAX_COLUMNS = 5;
    private static final int MAX_ROWS = 4;

    public Actions(final Mechanics mechanics) {
    }

    /**
     * functie care ataca o carte
     * @param xAttacker este linia cartii care ataca
     * @param yAttacker este coloana cartii care ataca
     * @param xDefender este linia cartii care este atacata
     * @param yDefender este coloana cartii care este atacata
     */

    public void cardUsed(final int xAttacker, final int yAttacker,
                         final int xDefender, final int yDefender) {
        Card attacker = Mechanics.getCardMatrix()[xAttacker][yAttacker];
        if (attacker == null) {
            return;
        }

        /**
         * verific daca atac o carte a coechipierului
         * @Mechanics.game.getTurn() imi spune randul carui
         * jucator este
         */
        if ((xDefender >= 2 && Mechanics.game.getTurn() == 1)
                || (xDefender <= 1 && Mechanics.game.getTurn() == 2)) {
            Error.attackCard = NOT_ENEMY_CARD;
            return;
        }

        /**
         * verific daca a mai fost folosita cartea
         */
         if (attacker.isPlayed() || attacker.isAbilityUsed()) {
            Error.attackCard = ATTACK;
            return;
        }

        /**
         * verific cazul in care cartea este frozen
         */
        if (attacker.getFrozen() > 0) {
            Error.attackCard = FROZEN;
        }

        /**
         * verific daca exista carti de tip "tank" si
         * daca a fost selectata pentru atac
         */
         if (Mechanics.game.getTurn() == 1 && xDefender == 0) {
            for (int i = 0; i < MAX_COLUMNS; i++) {
                Card card = Mechanics.getCardMatrix()[1][i];
                if (card != null && (card.getName().equals("Goliath")
                        || card.getName().equals("Warden"))) {
                    Error.attackCard = TANK;
                    return;
                }
            }
        }
         if (Mechanics.game.getTurn() == 2 && xDefender == MAX_ROWS - 1) {
            for (int i = 0; i < MAX_COLUMNS; i++) {
                Card card = Mechanics.getCardMatrix()[2][i];
                if (card != null && (card.getName().equals("Goliath")
                        || card.getName().equals("Warden"))) {
                        Error.attackCard = TANK;
                        return;
                    }
            }
        }
        attacker.setPlayed(true);
        Card deffender = Mechanics.getCardMatrix()[xDefender][yDefender];

        if (deffender.getHealth() <= attacker.getAttackDamage()) {
            Mechanics.getCardMatrix()[xDefender][yDefender] = null;
            for (int i = yDefender; i < MAX_COLUMNS - 1; i++) {
                Mechanics.getCardMatrix()[xDefender][i] =
                        Mechanics.getCardMatrix()[xDefender][i + 1];
            }
            Mechanics.getCardMatrix()[xDefender][MAX_COLUMNS - 1] = null;
            // Ultima poziție devine null
        } else {
            Mechanics.getCardMatrix()[xDefender][yDefender].setHealth(
                    Mechanics.getCardMatrix()[xDefender][yDefender].getHealth()
                            - attacker.getAttackDamage());
            }

    }

    /**
     * functie care foloseste abilitatea unei carti
     * @param xAttacker este linia cartii care ataca
     * @param yAttacker este coloana cartii care ataca
     * @param xDefender este linia cartii care este atacata
     * @param yDefender este coloana cartii care este atacata
     */

    public void cardUsesAbility(final int xAttacker, final int yAttacker,
                                final int xDefender,  final int yDefender) {
        Card attacker = Mechanics.getCardMatrix()[xAttacker][yAttacker];
        Card defender = Mechanics.getCardMatrix()[xDefender][yDefender];

        if (defender == null || attacker == null) {
            return;
        }

        if (attacker.getFrozen() > 0) {
            Error.cardAbility = FROZEN;
            return;
        } else if (attacker.isPlayed() || attacker.isAbilityUsed()) {
            Error.cardAbility = ATTACK;
            return;
        } else if (attacker.getName().equals("Disciple")
                && ((Mechanics.game.getTurn() == 1 && xDefender <= 1)
                || (Mechanics.game.getTurn() == 2 && xDefender >= 2))) {
            Error.cardAbility = NOT_ALLY_CARD;
            return;
        } else if ((attacker.getName().equals("The Ripper")
                || attacker.getName().equals("Miraj")
                || attacker.getName().equals("The Cursed One"))) {
            if (((Mechanics.game.getTurn() == 1 && xDefender >= 2)
                    || (Mechanics.game.getTurn() == 2 && xDefender <= 1))) {
                Error.cardAbility = NOT_ENEMY_CARD;
                return;
            } else if (Mechanics.game.getTurn() == 1) {
                for (int i = 0; i < MAX_ROWS; i++) {
                    Card card = Mechanics.getCardMatrix()[1][i];
                    if (card != null && (card.getName().equals("Goliath")
                            || card.getName().equals("Warden"))) {
                        if (yDefender != i) {
                            Error.cardAbility = TANK;
                        } else {
                            Error.cardAbility = 0;
                            break;
                        }
                    }
                }

            } else if (Mechanics.game.getTurn() == 2) {
                for (int i = 0; i < MAX_COLUMNS; i++) {
                    Card card = Mechanics.getCardMatrix()[2][i];
                    if (card != null && (card.getName().equals("Goliath")
                            || card.getName().equals("Warden"))) {
                        if (yDefender  != i) {
                                Error.cardAbility = TANK;
                        } else {
                            Error.cardAbility = 0;
                            break;
                        }
                    }

                }
            }
        }

        /**
         * aplic abilitatea fiecarei carti in parte
         */
        if (Error.cardAbility == 0) {
            attacker.setAbilityUsed(true);
        switch (attacker.getName()) {
            case "The Ripper":
                defender.setAttackDamage(Math.max(0, defender.getAttackDamage() - 2));
                break;
            case "Miraj":
                int aux = defender.getHealth();
                defender.setHealth(Math.max(0, attacker.getHealth()));
                attacker.setHealth(aux);
                break;
            case "The Cursed One":
                aux = defender.getHealth();
                defender.setHealth(defender.getAttackDamage());
                defender.setAttackDamage(aux);
                if (defender.getHealth() == 0) {
                    Mechanics.getCardMatrix()[xDefender][yDefender] = null;
                    for (int i = yDefender; i < MAX_COLUMNS - 1; i++) {
                        if (Mechanics.getCardMatrix()[xDefender][i + 1] != null) {
                            Mechanics.getCardMatrix()[xDefender][i] =
                                    Mechanics.getCardMatrix()[xDefender][i + 1];
                        }
                    }
                    Mechanics.getCardMatrix()[xDefender][MAX_COLUMNS - 1] = null;
                }
                break;
            case "Disciple":
                defender.setHealth(defender.getHealth() + 2);
                break;
            default:
                break;
        }
        }
    }

    /**
     * functia care ataca eroul unui jucator
     * @param xAttacker linia pe care se afla cartea care ataca
     * @param yAttacker coloana pe care se afla cartea care ataca
     */

    public void attackHero(final int xAttacker, final int yAttacker) {
        Card attacker = Mechanics.getCardMatrix()[xAttacker][yAttacker];
        Player hero1 = Mechanics.game.getPlayer1();
        Player hero2 = Mechanics.game.getPlayer2();

        if (attacker == null) {
            return;
        }

        if (attacker.getFrozen() > 0) {
            Error.attackCard = FROZEN;
            return;
        }

        if (attacker.isPlayed() || attacker.isAbilityUsed()) {
            Error.attackCard = ATTACK;
            return;
        }
        if ((Mechanics.game.getTurn() == 1)) {
            for (int i = 0; i < MAX_COLUMNS; i++) {
                Card card = Mechanics.getCardMatrix()[1][i];
                if (card != null) {
                    if (card.getName().equals("Goliath")
                            || card.getName().equals("Warden")) {
                        Error.attackCard = TANK;
                        return;
                    }
                }
            }
        } else if ((Mechanics.game.getTurn() == 2)) {
            for (int i = 0; i < MAX_COLUMNS; i++) {
                Card card = Mechanics.getCardMatrix()[2][i];
                if (card != null) {
                    if (card.getName().equals("Goliath")
                            || card.getName().equals("Warden")) {
                        Error.attackCard = TANK;
                        return;
                    }
                }
            }
        }

        if (Mechanics.game.getTurn() == 1 && Error.attackCard == 0) {

            if (hero2.getHero() != null && hero2.getHero().getHealth() > 0) {
                int score = Mechanics.game.getPlayer1Score();
                attacker.setPlayed(true);
                int newHealth = hero2.getHero().getHealth()
                        - attacker.getAttackDamage();
                hero2.getHero().setHealth(newHealth);

                if (newHealth <= 0) {
                    Mechanics.game.setPlayer1Score(score + 1);
                }
            }
        } else if (Mechanics.game.getTurn() == 2 && Error.attackCard == 0) {
            int score = Mechanics.game.getPlayer2Score();
            if (hero1.getHero() != null && hero1.getHero().getHealth() > 0) {
                attacker.setPlayed(true);
                int newHealth = hero1.getHero().getHealth()
                        - attacker.getAttackDamage();
                hero1.getHero().setHealth(newHealth);

                if (newHealth <= 0) {
                    Mechanics.game.setPlayer2Score(score + 1);
                }
            }
        }
    }

    /**
     * aici folosesc abilitatea eroului daca respecta conditiile
     * @param turn obtine tura jucatorului curent
     * @param row obtine linia pe care trebuie aplicat efectul
     */
    public void useHeroAbility(final int turn, final int row) {

        Player player = Mechanics.game.getPlayer(turn);
        if (player == null || player.getHero() == null) {
            return;
        }

        Card hero = player.getHero();
        if (hero.getMana() > player.getMana()) {
            Error.herroAttackError = MANA;
            return;
        } else if (hero.isAbilityUsed()) {
            Error.herroAttackError = ATTACK;
            return;
        }

        /**
         * verific conditiile pentru fiecare erou si ii aplic
         * abilitatea daca randul selectat este valid
         */
        switch (hero.getName()) {
            case "Lord Royce" -> {
                if ((turn == 1 && row >= 2) || (turn == 2 && row <= 1)) {
                    Error.herroAttackError = NOT_ENEMY_CARD;
                    return;
                }
                for (Card card : Mechanics.getCardMatrix()[row]) {
                    if (card != null) {
                        card.setFrozen(2);
                    }
                }
                hero.setAbilityUsed(true);
                player.setMana(player.getMana() - hero.getMana());
            }

            case "Empress Thorina" -> {
                if ((turn == 1 && row >= 2) || (turn == 2 && row <= 1)) {
                    Error.herroAttackError = NOT_ENEMY_CARD;
                    return;
                }
                int maxHealth = -1, index = -1;
                for (int i = 0; i < Mechanics.getCardMatrix()[row].length; i++) {
                    Card card = Mechanics.getCardMatrix()[row][i];
                    if (card != null && card.getHealth() > maxHealth) {
                        maxHealth = card.getHealth();
                        index = i;
                    }
                }
                if (index != -1) {
                    for (int i = index; i < Mechanics.getCardMatrix()[row].length - 1; i++) {
                        Mechanics.getCardMatrix()[row][i] =
                                Mechanics.getCardMatrix()[row][i + 1];
                    }
                    Mechanics.getCardMatrix()[row][Mechanics.getCardMatrix()[row].length - 1] =
                            null;
                    hero.setAbilityUsed(true);
                    player.setMana(player.getMana() - hero.getMana());
                }
            }

            case "King Mudface" -> {
                if ((turn == 1 && row <= 1) || (turn == 2 && row >= 2)) {
                    Error.herroAttackError = NOT_ALLY_CARD;
                    return;
                }
                for (Card card : Mechanics.getCardMatrix()[row]) {
                    if (card != null) {
                        card.setHealth(card.getHealth() + 1);
                    }
                }
                hero.setAbilityUsed(true);
                player.setMana(player.getMana() - hero.getMana());
            }

            case "General Kocioraw" -> {
                if ((turn == 1 && row <= 1) || (turn == 2 && row >= 2)) {
                    Error.herroAttackError = NOT_ALLY_CARD;
                    return;
                }
                for (Card card : Mechanics.getCardMatrix()[row]) {
                    if (card != null) {
                        card.setAttackDamage(card.getAttackDamage() + 1);
                    }
                }
                hero.setAbilityUsed(true);
                player.setMana(player.getMana() - hero.getMana());
            }
            default -> {
            }
        }
    }
}
