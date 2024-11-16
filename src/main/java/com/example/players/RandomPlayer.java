package com.example.players;

import java.util.List;
import com.example.logic.Board;
import com.example.logic.Color;
import com.example.logic.Game;
import com.example.logic.Move;

/**
 * Represents a player that makes random moves in a game.
 */
public class RandomPlayer extends Player {

    public RandomPlayer(String name, Color color) {
        super(name, color);
    }

    /**
     * Generates a random move for the player.
     *
     * @param game The current game state.
     * @return The randomly selected move.
     */
    @Override
    public Move move(Game game) {
        // Get all possible moves on the current board
        List<Move> moves = Board.getPossibleMoves(game);

        // If there are any possible moves
        if (!moves.isEmpty()) {
            // Generate a random index within the range of possible moves
            int randomIndex = (int) (Math.random() * moves.size());

            // Return the randomly selected move
            return moves.get(randomIndex);
        }

        // If there are no possible moves, return null
        System.out.println(game.getBoard());
        return null;
    }

    @Override
    public Player clone() {
        return new RandomPlayer(this.getName(), this.getColor());
    }

}
