package com.example.players;

import java.util.ArrayList;
import java.util.List;
import com.example.logic.Card;
import com.example.logic.Color;
import com.example.logic.Game;
import com.example.logic.Move;
import com.example.logic.Piece;

/**
 * Represents a player in the game.
 */
public abstract class Player {

    private final String name; // The name of the player
    private final Color color; // The color of the player's pieces
    private List<Piece> pieces; // The pieces owned by the player
    private List<Card> cards; // The movement cards held by the player

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        pieces = new ArrayList<>();
        cards = new ArrayList<>();
    }

    /**
     * Abstract method for making a move in the game.
     *
     * @param game The current game state.
     * @return The move chosen by the player.
     */
    public abstract Move move(Game game);

    /**
     * Abstract method for cloning the player.
     *
     * @param player The player to clone.
     * @return The cloned player.
     */
    @Override
    public abstract Player clone();

    /**
     * Get the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the color of the player's pieces.
     *
     * @return The color of the player's pieces.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Set the pieces owned by the player.
     *
     * @param pieces The pieces owned by the player.
     */
    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }

    /**
     * Get the pieces owned by the player.
     *
     * @return The pieces owned by the player.
     */
    public List<Piece> getPieces() {
        return this.pieces;
    }

    /**
     * Get the movement cards held by the player.
     *
     * @return The movement cards held by the player.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Get a copy of the movement cards held by the player.
     *
     * @return A copy of the movement cards held by the player.
     */
    public List<Card> getCardsCopy() {
        return new ArrayList<>(cards);
    }

    /**
     * Set the movement cards held by the player.
     *
     * @param movementCards The movement cards held by the player.
     */
    public void setCards(List<Card> movementCards) {
        this.cards = movementCards;
    }

}
