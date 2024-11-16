package com.example.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a card in the Onitama game. Each card has a name, a list of
 * possible moves, and a color.
 */
public class Card {

    private final String name; // Name of the card
    private final List<int[]> movements; // List of possible movements for the card
    private final Color color; // Color of the card

    /**
     * Creates a new Card with the given name, movements, and color.
     *
     * @param name the name of the card
     * @param moves the list of movements associated with the card
     * @param color the color of the card
     */
    public Card(String name, List<int[]> moves, Color color) {
        this.name = name;
        this.movements = moves;
        this.color = color;
    }

    /**
     * Initializes a list of cards with their respective names, movements, and
     * colors.
     *
     * @return the list of initialized cards
     */
    public static List<Card> initializeCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Boar", Arrays.asList(new int[]{0, -1}, new int[]{0, 1}, new int[]{1, 0}), Color.RED));
        cards.add(new Card("Elephant", Arrays.asList(new int[]{0, -1}, new int[]{0, 1}, new int[]{1, -1}, new int[]{1, 1}), Color.RED));
        cards.add(new Card("Mantis", Arrays.asList(new int[]{-1, 0}, new int[]{1, -1}, new int[]{1, 1}), Color.RED));
        cards.add(new Card("Crane", Arrays.asList(new int[]{-1, -1}, new int[]{-1, 1}, new int[]{1, 0}), Color.BLUE));
        cards.add(new Card("Monkey", Arrays.asList(new int[]{-1, -1}, new int[]{-1, 1}, new int[]{1, -1}, new int[]{1, 1}), Color.BLUE));
        cards.add(new Card("Crab", Arrays.asList(new int[]{0, -2}, new int[]{0, 2}, new int[]{1, 0}), Color.BLUE));
        cards.add(new Card("Tiger", Arrays.asList(new int[]{-1, 0}, new int[]{2, 0}), Color.BLUE));
        cards.add(new Card("Dragon", Arrays.asList(new int[]{-1, -1}, new int[]{-1, 1}, new int[]{1, -2}, new int[]{1, 2}), Color.RED));
        cards.add(new Card("Horse", Arrays.asList(new int[]{-1, 0}, new int[]{0, -1}, new int[]{1, 0}), Color.RED));
        cards.add(new Card("Goose", Arrays.asList(new int[]{-1, 1}, new int[]{0, -1}, new int[]{0, 1}, new int[]{1, -1}), Color.BLUE));
        cards.add(new Card("Eel", Arrays.asList(new int[]{-1, -1}, new int[]{0, 1}, new int[]{1, -1}), Color.BLUE));
        cards.add(new Card("Frog", Arrays.asList(new int[]{-1, 1}, new int[]{0, -2}, new int[]{1, -1}), Color.RED));
        cards.add(new Card("Ox", Arrays.asList(new int[]{0, 1}, new int[]{-1, 0}, new int[]{1, 0}), Color.BLUE));
        cards.add(new Card("Rooster", Arrays.asList(new int[]{-1, -1}, new int[]{0, -1}, new int[]{0, 1}, new int[]{1, 1}), Color.RED));
        cards.add(new Card("Cobra", Arrays.asList(new int[]{-1, 1}, new int[]{0, -1}, new int[]{1, 1}), Color.RED));
        cards.add(new Card("Rabbit", Arrays.asList(new int[]{-1, -1}, new int[]{0, 2}, new int[]{1, 1}), Color.BLUE));

        return cards;
    }

    /**
     * Returns the name of the card.
     *
     * @return the name of the card
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the list of possible movements for the card.
     *
     * @return the list of possible movements
     */
    public List<int[]> getMovements() {
        return this.movements;
    }

    public List<int[]> getMovementsCopy() {
        List<int[]> movementsCopy = new ArrayList<>();
        for (int[] movement : movements) {
            movementsCopy.add(new int[]{movement[0], movement[1]});
        }
        return movementsCopy;
    }

    /**
     * Returns the color of the card.
     *
     * @return the color of the card
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Prints the list of possible movements for the card.
     */
    public void printMovements() {
        for (int[] movement : movements) {
            System.out.println("Movement: (" + movement[0] + ", " + movement[1] + ")");
        }
    }

    @Override
    public String toString() {
        return "Card{" + "name='" + name + '\'' + ", color=" + color + '}';
    }

    @Override
    public Card clone() {
        return new Card(this.name, this.getMovementsCopy(), this.color);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card card = (Card) obj;
        return name.equals(card.name);
    }
}
