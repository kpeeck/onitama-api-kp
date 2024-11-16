package com.example.players;

import java.util.List;
import java.util.Scanner;
import com.example.logic.Board;
import com.example.logic.Color;
import com.example.logic.Game;
import com.example.logic.Move;

/**
 * Represents a human player in the game. Extends the abstract class Player.
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String name, Color color) {
        super(name, color);
    }

    /**
     * Implements the move logic for the HumanPlayer.
     *
     * @param game The current game state.
     * @return The move chosen by the HumanPlayer.
     */
    @Override
    public Move move(Game game) {
        List<Move> moves = Board.getPossibleMoves(game);

        if (moves.isEmpty()) {
            return null;
        }

        System.out.println("Possible moves:");

        // Print all possible moves
        for (int i = 0; i < moves.size(); i++) {
            System.out.println(i + ": " + moves.get(i));
        }

        // Get the input from the user
        Scanner reader = new Scanner(System.in); // Reading from System.in
        int n = -1;
        while (n < 0 || n >= moves.size()) {
            System.out.println("Enter a number between 0 and " + (moves.size() - 1) + ":");
            if (reader.hasNextInt()) {
                n = reader.nextInt(); // Scans the next token of the input as an int
                if (n < 0 || n >= moves.size()) {
                    System.out.println("Invalid input. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                reader.next(); // Consume the invalid input
            }
        }

        return moves.get(n);
    }

    @Override
    public Player clone() {
        return new HumanPlayer(this.getName(), this.getColor());
    }

}
