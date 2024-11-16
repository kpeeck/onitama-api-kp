package com.example.logic;

/**
 * Represents a piece in the game.
 */
public class Piece {

    private final PieceType type; // The type of the piece
    public final Color color; // The color of the piece
    private Tile tile; // The tile where the piece is located

    public Piece(PieceType type) {
        this.type = type;
        this.color = type == PieceType.BLUEMASTER || type == PieceType.BLUESTUDENT ? Color.BLUE : Color.RED;
    }

    /**
     * Gets the type of the piece.
     *
     * @return The type of the piece.
     */
    public PieceType getType() {
        return this.type;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Returns a string representation of the piece.
     *
     * @return The string representation of the piece.
     */
    @Override
    public String toString() {
        return this.type.toString();
    }

    /**
     * Gets the tile where the piece is located.
     *
     * @return The tile where the piece is located.
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Sets the tile where the piece is located.
     *
     * @param tile The tile where the piece is located.
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Clones the piece.
     *
     * @return A clone of the piece.
     */
    public Piece clone() {
        Piece clone = new Piece(this.type);
        return clone;
    }

    /**
     * Checks if two pieces are equal.
     *
     * @param obj The object to compare.
     * @return True if the pieces are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Piece)) {
            return false;
        }
        Piece piece = (Piece) obj;
        return this.type == piece.type && this.color == piece.color && this.tile.getX() == piece.tile.getX()
                && this.tile.getY() == piece.tile.getY();
    }
}
