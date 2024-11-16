package com.example.logic;

/**
 * Represents a tile on the game board. Each tile has an x-coordinate,
 * y-coordinate, and may contain a piece. It can also be a blue temple or a red
 * temple.
 */
public class Tile {

    private final int x; // x-coordinate of the tile
    private final int y; // y-coordinate of the tile
    private Piece piece; // the piece on the tile
    private boolean blueTemple; // indicates if the tile is a blue temple
    private boolean redTemple; // indicates if the tile is a red temple

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the piece on the tile.
     *
     * @param piece the piece to be set on the tile
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Gets the piece on the tile.
     *
     * @return the piece on the tile
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * Gets the x-coordinate of the tile.
     *
     * @return the x-coordinate of the tile
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the y-coordinate of the tile.
     *
     * @return the y-coordinate of the tile
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets the tile as a blue temple.
     */
    public void setBlueTemple() {
        this.blueTemple = true;
    }

    /**
     * Checks if the tile is a blue temple.
     *
     * @return true if the tile is a blue temple, false otherwise
     */
    public boolean isBlueTemple() {
        return this.blueTemple;
    }

    /**
     * Sets the tile as a red temple.
     */
    public void setRedTemple() {
        this.redTemple = true;
    }

    /**
     * Checks if the tile is a red temple.
     *
     * @return true if the tile is a red temple, false otherwise
     */
    public boolean isRedTemple() {
        return this.redTemple;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public String printPiece() {
        if (this.piece == null) {
            return "  "; // represents an empty tile
        }
        return this.piece.toString();
    }

    public Tile clone() {
        Tile newTile = new Tile(this.x, this.y);
        if (this.piece != null) {
            Piece clone = this.piece.clone();
            newTile.setPiece(clone);
            clone.setTile(newTile);
        }
        if (this.blueTemple) {
            newTile.setBlueTemple();
        }
        if (this.redTemple) {
            newTile.setRedTemple();
        }
        return newTile;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tile tile = (Tile) obj;
        return x == tile.x && y == tile.y && blueTemple == tile.blueTemple && redTemple == tile.redTemple;
        // && (piece != null ? piece.equals(tile.piece) : tile.piece == null);
    }

}
