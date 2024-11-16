package com.example.logic;

/**
 * Represents the types of pieces in the game. The available piece types are
 * BLUEMASTER, BLUESTUDENT, REDMASTER, and REDSTUDENT.
 */
public enum PieceType {
    BLUEMASTER, // Represents the blue master piece
    BLUESTUDENT, // Represents the blue student piece
    REDMASTER, // Represents the red master piece
    REDSTUDENT; // Represents the red student piece

    /**
     * Returns a string representation of the piece type.
     *
     * @return The string representation of the piece type.
     */
    @Override
    public String toString() {
        return switch (this) {
            case BLUEMASTER ->
                "BM";
            case BLUESTUDENT ->
                "BS";
            case REDMASTER ->
                "RM";
            case REDSTUDENT ->
                "RS";
            default ->
                null;
        };
    }
}
