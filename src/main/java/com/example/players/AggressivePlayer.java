package com.example.players;

import java.util.ArrayList;
import java.util.List;
import com.example.logic.Board;
import com.example.logic.Color;
import com.example.logic.Game;
import com.example.logic.Move;

public class AggressivePlayer extends Player {

    public AggressivePlayer(String name, Color color) {
        super(name, color);
    }

    /**
     * Implements the move logic for the AggressivePlayer.
     *
     * @param game The current game state.
     * @return The move chosen by the AggressivePlayer.
     */
    @Override
    public Move move(Game game) {
        // Get all possible moves on the current board
        List<Move> moves = Board.getPossibleMoves(game);

        List<Move> aggressiveMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move.getTarget().getPiece() != null && move.getTarget().getPiece().getColor() != this.getColor()) {
                aggressiveMoves.add(move);
            }
        }

        // If there are any aggressive moves
        if (!aggressiveMoves.isEmpty()) {
            // Generate a random index within the range of aggressive moves
            int randomIndex = (int) (Math.random() * aggressiveMoves.size());

            // Return the randomly selected aggressive move
            return aggressiveMoves.get(randomIndex);
        }

        // If there are any possible moves
        if (!moves.isEmpty()) {
            // Generate a random index within the range of possible moves
            int randomIndex = (int) (Math.random() * moves.size());

            // Return the randomly selected move
            return moves.get(randomIndex);
        }

        // If there are no possible moves, return null
        return null;
    }

    @Override
    public Player clone() {
        return new AggressivePlayer(this.getName(), this.getColor());
    }
}
