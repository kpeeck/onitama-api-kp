package com.example.logic;

import java.util.ArrayList;
import java.util.List;
import com.example.players.Player;

/**
 * Represents a game board in the Onitama game. The board consists of a grid of
 * tiles and holds the pieces and their positions.
 */
public class Board {

    private final int SIZE = 7; // The size of the board
    private final Tile[][] tiles; // The array of tiles representing the board

    /**
     * Constructs a new Board object.
     */
    public Board() {
        this.tiles = new Tile[SIZE][SIZE]; // Create a new array of tiles
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.tiles[i][j] = new Tile(i, j); // Create a new tile at position (i, j)
                // Set the temple tiles
                if (j == (int) (SIZE / 2)) {
                    if (i == 0) {
                        this.tiles[i][j].setBlueTemple(); // Set the tile as a blue temple
                    } else if (i == SIZE - 1) {
                        this.tiles[i][j].setRedTemple(); // Set the tile as a red temple
                    }
                }
            }
        }
    }

    /**
     * Constructs a new Board object by copying the given board.
     *
     * @param board the board to be copied
     */
    public Board(Board board) {
        this.tiles = new Tile[SIZE][SIZE]; // Create a new array of tiles
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.tiles[i][j] = board.tiles[i][j].clone(); // Create a new tile at position (i, j)
            }
        }
    }

    /**
     * Returns the tile at the specified position.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the tile at the specified position
     */
    public Tile getTile(int x, int y) {
        return this.tiles[x][y]; // Return the tile at position (x, y)
    }

    /**
     * Initializes the board with the initial positions of the pieces for the
     * two players.
     *
     * @param player1 the first player
     * @param player2 the second player
     */
    public void initializeBoard(Player player1, Player player2) {
        // Set up the initial positions of all pieces on the board and return a list of all pieces
        List<Piece> bluePieces = player1.getPieces();
        List<Piece> redPieces = player2.getPieces();
        for (int i = 0; i < SIZE; i++) {
            if (i != (int) (SIZE / 2)) {
                Piece blueStudent = new Piece(PieceType.BLUESTUDENT); // Create a new blue student piece
                blueStudent.setTile(tiles[0][i]); // Set the tile of the piece
                bluePieces.add(blueStudent); // Add the piece to the list of blue pieces
                this.tiles[0][i].setPiece(blueStudent); // Set the piece on the tile
                Piece redStudent = new Piece(PieceType.REDSTUDENT); // Create a new red student piece
                redStudent.setTile(tiles[(SIZE - 1)][i]); // Set the tile of the piece
                redPieces.add(redStudent); // Add the piece to the list of red pieces
                this.tiles[(SIZE - 1)][i].setPiece(redStudent); // Set the piece on the tile
            } else {
                Piece blueMaster = new Piece(PieceType.BLUEMASTER); // Create a new blue master piece
                blueMaster.setTile(tiles[0][i]); // Set the tile of the piece
                bluePieces.add(blueMaster); // Add the piece to the list of blue pieces
                this.tiles[0][i].setPiece(blueMaster); // Set the piece on the tile
                Piece redMaster = new Piece(PieceType.REDMASTER); // Create a new red master piece
                redMaster.setTile(tiles[(SIZE - 1)][i]); // Set the tile of the piece
                redPieces.add(redMaster); // Add the piece to the list of red pieces
                this.tiles[(SIZE - 1)][i].setPiece(redMaster); // Set the piece on the tile
            }
        }
    }

    /**
     * Returns a list of all possible moves for the current player in the given
     * game.
     *
     * @param game the game
     * @return a list of all possible moves
     */
    public static List<Move> getPossibleMoves(Game game) {
        List<Move> possibleMoves = new ArrayList<>();
        for (Piece piece : game.getCurrentPlayer().getPieces()) {
            for (Card card : game.getCurrentPlayer().getCardsCopy()) {
                for (int[] movement : card.getMovementsCopy()) {
                    if (game.getCurrentPlayer().getColor() == Color.RED) {
                        movement[0] *= -1;
                        movement[1] *= -1;
                    }
                    if (isValidMove(game, piece, card, movement)) {
                        possibleMoves.add(new Move(card, piece, movement, piece.getTile(),
                                game.getBoard().getTile(
                                        piece.getTile().getX() + movement[0], piece.getTile().getY() + movement[1])));
                        // Add the move to the list of possible moves
                    }
                }
            }
        }
        return possibleMoves; // Return the list of possible moves
    }

    /**
     * Checks if a movement is valid in the given game.
     *
     * @param game the game
     * @param piece the piece to be moved
     * @param card the card used for the movement
     * @param movement the movement to be made
     * @return true if the movement is valid, false otherwise
     */
    public static boolean isValidMove(Game game, Piece piece, Card card, int[] movement) {
        // Check if the movement is valid
        // Check whether the piece belongs to the current player
        if (!game.getCurrentPlayer().getPieces().contains(piece)) {
            return false;
        }
        // Check whether the card belongs to the current player
        if (!game.getCurrentPlayer().getCards().contains(card)) {
            return false;
        }
        // Check whether the movement is in the list of possible moves for the card
        List<int[]> possibleMovements = card.getMovementsCopy();
        if (game.getCurrentPlayer().getColor() == Color.RED) {
            movement[0] *= -1;
            movement[1] *= -1;
        }
        boolean contained = false;
        for (int[] possibleMovement : possibleMovements) {
            if (possibleMovement[0] == movement[0] && possibleMovement[1] == movement[1]) {
                contained = true;
                break;
            }
        }
        if (game.getCurrentPlayer().getColor() == Color.RED) {
            movement[0] *= -1;
            movement[1] *= -1;
        }
        if (!contained) {
            return false;
        }
        // Check whether the movement is within the bounds of the board
        int newX = piece.getTile().getX() + movement[0];
        int newY = piece.getTile().getY() + movement[1];
        if (newX < 0 || newX >= game.getBoard().SIZE || newY < 0 || newY >= game.getBoard().SIZE) {
            return false;
        }
        // Check whether the target tile is occupied by a piece of the current player
        Tile targetTile = game.getBoard().getTile(newX, newY);
        if (targetTile.getPiece() != null) {
            // Check whether the target piece belongs to the current player
            if (game.getCurrentPlayer().getPieces().contains(targetTile.getPiece())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the size of the board.
     *
     * @return the tiles
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    @Override
    public Board clone() {
        return new Board(this);
    }

    // /**
    //  * Prints the current state of the board.
    //  */
    // public void printBoard() {
    //     for (int i = 0; i < SIZE; i++) {
    //         for (int j = 0; j < SIZE; j++) {
    //             System.out.print(this.tiles[i][j].printPiece() + " "); // Print the tile
    //         }
    //         System.out.println();
    //     }
    // }
    /**
     * Returns a string representation of the board.
     *
     * @return The string representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = SIZE - 1; i >= 0; i--) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(this.tiles[i][j].printPiece()).append(" "); // Append the tile to the string builder
            }
            sb.append("\n");
        }
        return sb.toString(); // Return the string representation of the board
    }

    /**
     * Checks if two boards are equal.
     *
     * @param obj The object to compare.
     * @return True if the boards are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Board board = (Board) obj;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!this.tiles[i][j].equals(board.tiles[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

}
