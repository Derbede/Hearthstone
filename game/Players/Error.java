package org.poo.game.Players;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Error {

    static final ObjectMapper objectMapper = new ObjectMapper();
    public static int herroAttackError;
    public static int attackCard;
    public static int cardAbility;

    private static final int ATTACK = 3;
    private static final int NOT_ENEMY_CARD = 4;
    private static final int NOT_ALLY_CARD = 5;
    private static final int MANA = 6;

    /**
     * functie care scrie mesajul de eroare in fisierul de iesire
     * @param playerIdx returneaza playerul care nu are suficienta mana
     * @return returneaza mesajul de eroare corespunzator
     */
    public static ObjectNode noMana(final int playerIdx) {
        ObjectNode objectNode = objectMapper.createObjectNode();

                objectNode.put("command", "placeCard");
                objectNode.put("error", "Not enough mana to place card on table.");
                objectNode.put("handIdx", playerIdx);

        return objectNode;

    }

    /**
     * returneaza mesajul de eroare pentru plasarea unei carti
     * @param x linia la care trebuia plasata cartea
     * @param y coloana la care trebuia plasata cartea
     * @return returneaza mesajul de eroare
     */

    public static ObjectNode cardAvailable(final int x, final int y) {
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getCardAtPosition");
        objectNode.put("output", "No card available at that position.");
        objectNode.put("x", x);
        objectNode.put("y", y);

        return objectNode;
    }

    /**
     * functia afiseaza in fisierul de output JSON mesajul de eroare pentru
     * folosirea abilitaii unui erou
     * @param affectedRow reprezinta linia asupra careia se aplica puterea
     *                    eroului
     * @param error este tipul de eroare
     * @return se returneaza mesajul
     */

    public static ObjectNode heroAbility(final int affectedRow, final int error) {
        // Dacă error este 0, nu facem nimic și returnăm null
        if (error == 0) {
            return null;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("affectedRow", affectedRow);
        objectNode.put("command", "useHeroAbility");

        switch (error) {
            case NOT_ENEMY_CARD -> objectNode.put("error",
                    "Selected row does not belong to the enemy.");
            case ATTACK -> objectNode.put("error", "Hero has already attacked this turn.");
            case MANA -> objectNode.put("error", "Not enough mana to use hero's ability.");
            case NOT_ALLY_CARD -> objectNode.put("error",
                    "Selected row does not belong to the current player.");
            default -> {
                return null;
            }
        }

        return objectNode;
    }

    /**
     * functie care scrie in fisierul de iesire JSON, mesajele de eroare in cazul
     * atacului unei carti asupra altei carti
     * @param xAttacker este linia cartii care ataca
     * @param yAttacker este coloana cartii care ataca
     * @param xDefender este linia cartii care este atacata
     * @param yDefender este coloana cartii care este atacata
     * @param error este tipul de eroare care trebuie afisat
     * @return returneaza mesajul la final
     */


    public static ObjectNode cardAttack(final int xAttacker, final int yAttacker,
                                        final int xDefender, final int yDefender,
                                        final int error) {
        if (error == 0) {
            return null;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        ObjectNode cardAttackerNode = objectMapper.createObjectNode();
        ObjectNode cardAttackedNode = objectMapper.createObjectNode();

        switch (error) {
            case 1 -> {
                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "useAttackHero");
                objectNode.put("error", "Attacker card is frozen.");
            }
            case 2 -> {
                cardAttackedNode.put("x", xDefender);
                cardAttackedNode.put("y", yDefender);
                objectNode.set("cardAttacked", cardAttackedNode);

                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "cardUsesAttack");
                objectNode.put("error", "Attacked card is not of type 'Tank'.");
            }
            case ATTACK -> {
                cardAttackedNode.put("x", xDefender);
                cardAttackedNode.put("y", yDefender);
                objectNode.set("cardAttacked", cardAttackedNode);

                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "cardUsesAttack");
                objectNode.put("error", "Attacker card has already attacked this turn.");


            }
            case NOT_ENEMY_CARD -> {
                cardAttackedNode.put("x", xDefender);
                cardAttackedNode.put("y", yDefender);
                objectNode.set("cardAttacked", cardAttackedNode);

                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "cardUsesAttack");
                objectNode.put("error", "Attacked card does not belong to the enemy.");
            }
            default -> {
                return null;
            }
        }

        return objectNode;
    }

    /**
     * functie care scrie in fisierul de iesire JSON, mesajele de eroare in functie
     * de abilitatea fiecarei carti si motivul erorii
     * @param xAttacker este linia cartii care ataca
     * @param yAttacker este coloana cartii care ataca
     * @param xDefender este linia cartii care este atacata
     * @param yDefender este coloana cartii care este atacata
     * @param error este tipul de eroare care trebuie afisat
     * @return returneaza mesajul la final
     */
    public static ObjectNode cardAbility(final int xAttacker,
                                         final int yAttacker, final int xDefender,
                                         final int yDefender, final int error) {

        ObjectNode objectNode = objectMapper.createObjectNode();
        ObjectNode cardAttackerNode = objectMapper.createObjectNode();
        ObjectNode cardAttackedNode = objectMapper.createObjectNode();

        switch (error) {
            case 1 -> {
                cardAttackedNode.put("x", xDefender);
                cardAttackedNode.put("y", yDefender);
                objectNode.set("cardAttacked", cardAttackedNode);

                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "cardUsesAbility");
                objectNode.put("error", "Attacker card is frozen.");

            }
            case 2 -> {
                cardAttackedNode.put("x", xDefender);
                cardAttackedNode.put("y", yDefender);
                objectNode.set("cardAttacked", cardAttackedNode);

                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "cardUsesAbility");
                objectNode.put("error", "Attacked card is not of type 'Tank'.");

            }
            case ATTACK -> {
                cardAttackedNode.put("x", xDefender);
                cardAttackedNode.put("y", yDefender);
                objectNode.set("cardAttacked", cardAttackedNode);

                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "cardUsesAbility");
                objectNode.put("error", "Attacker card has already attacked this turn.");


            }
            case NOT_ENEMY_CARD -> {
                cardAttackedNode.put("x", xDefender);
                cardAttackedNode.put("y", yDefender);
                objectNode.set("cardAttacked", cardAttackedNode);

                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "cardUsesAbility");
                objectNode.put("error", "Attacked card does not belong to the enemy.");
            }
            case NOT_ALLY_CARD -> {
                cardAttackedNode.put("x", xDefender);
                cardAttackedNode.put("y", yDefender);
                objectNode.set("cardAttacked", cardAttackedNode);

                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "cardUsesAbility");
                objectNode.put("error", "Attacked card does not belong to the current player.");
            }
            default -> {
                return null;
            }

        }

        return objectNode;
    }

    /**
     * functia care scrie mesajele de eroare in fisierul de tip JSON
     * @param xAttacker este linia cartii care ataca
     * @param yAttacker este coloana cartii care ataca
     * @param error reprezinta tipul de eroare
     * @return la final se returneaza mesajul
     */
    public static ObjectNode cardAttackHero(final int xAttacker,
                                            final int yAttacker, final int error) {

        ObjectNode objectNode = objectMapper.createObjectNode();
        ObjectNode cardAttackerNode = objectMapper.createObjectNode();

        switch (error) {
            case 1 -> {
                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "useAttackHero");
                objectNode.put("error", "Attacker card is frozen.");
            }
            case 2 -> {
                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "useAttackHero");
                objectNode.put("error", "Attacked card is not of type 'Tank'.");

            }
            case ATTACK -> {
                cardAttackerNode.put("x", xAttacker);
                cardAttackerNode.put("y", yAttacker);
                objectNode.set("cardAttacker", cardAttackerNode);

                objectNode.put("command", "useAttackHero");
                objectNode.put("error", "Attacker card has already attacked this turn.");
            }
            default -> {
                return null;
            }

        }

        return objectNode;
    }





}
