package com.example.logic;

import java.util.List;

/**
 * Represents a move in the game.
 *
 * This class encapsulates the information about a move, including the card
 * used, the piece being moved, and the movement vector.
 */
public class Move {

    private final Card card; // Represents the card used in the move
    private final Piece piece; // Represents the piece being moved
    private final int[] movement; // Represents the movement vector
    private final Tile origin; // Represents the origin tile
    private final Tile target; // Represents the target tile

    public Move(Card card, Piece piece, int[] movement, Tile Origin, Tile target) {
        this.card = card;
        this.piece = piece;
        this.movement = movement;
        this.origin = Origin;
        this.target = target;
    }

    // Get the equivalent move from the game instance
    public Move getEquivalentMove(Game game) {
        List<Move> possibleMoves = Board.getPossibleMoves(game);
        for (Move possibleMove : possibleMoves) {
            if (this.movement[0] == possibleMove.movement[0] && this.movement[1] == possibleMove.movement[1]
                    && this.origin.equals(possibleMove.origin)
                    && this.target.equals(possibleMove.target)
                    && this.card.equals(possibleMove.card)) {
                return possibleMove;
            }
        }
        return null;  // Or handle appropriately
    }

    // // Get the equivalent card from the cloned game state
    // public Card getEquivalentCard(Game clonedGame) {
    //     for (Card clonedCard : clonedGame.getCurrentPlayer().getCards()) {
    //         if (clonedCard.equals(this.card)) {
    //             return clonedCard;
    //         }
    //     }
    //     return null;  // Or handle appropriately
    // }
    // Get the equivalent piece from the cloned game state
    public Piece getEquivalentPiece(Game clonedGame) {
        for (Piece clonedPiece : clonedGame.getCurrentPlayer().getPieces()) {
            if (clonedPiece.equals(this.piece)) {
                return clonedPiece;
            }
        }
        return null;
    }

    /**
     * Returns the card used in the move.
     *
     * @return The card used in the move.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Returns the piece being moved.
     *
     * @return The piece being moved.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Returns the movement vector.
     *
     * @return The movement vector.
     */
    public int[] getMovement() {
        return movement;
    }

    /**
     * Returns a copy of the movement vector.
     *
     * @return A copy of the movement vector.
     */
    public int[] getMovementCopy() {
        return new int[]{movement[0], movement[1]};
    }

    /**
     * Returns the origin tile.
     *
     * @return The origin tile.
     */
    public Tile getOrigin() {
        return origin;
    }

    /**
     * Returns the target tile.
     *
     * @return The target tile.
     */
    public Tile getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Move move = (Move) obj;
        return card.equals(move.card) && piece.equals(move.piece) && movement[0] == move.movement[0]
                && movement[1] == move.movement[1];
    }

    @Override
    public int hashCode() {
        int result = card.hashCode();
        result = 31 * result + piece.hashCode();
        result = 31 * result + movement[0];
        result = 31 * result + movement[1];
        return result;
    }

    @Override
    public String toString() {
        return "Move{" + "card=" + card.getName() + ", piece=" + piece + ", origin=" + origin
                + ", target=" + target + ", movement=(" + movement[0] + "," + movement[1] + ")}";
    }
}
