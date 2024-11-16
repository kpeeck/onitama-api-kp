package com.example;

import com.example.logic.Color;
import com.example.logic.Game; // Import the Color enum from the logic package
import com.example.logic.Statistics; // Import the Game class from the logic package

/**
 * The Main class serves as the entry point for the Onitama game application. It
 * initializes a new Game instance and starts the game.
 */
public class Main {

    /**
     * The entry point for the Onitama game application.
     *
     * @param args
     */
    public static void main(String[] args) {
        // final int NUM_GAMES = 500; // Number of games to play
        final int hours = 1; // Time limit in hours
        final int TIME = 10 * 60 * 1000; // Time limit in milliseconds
        Game game;
        Statistics statistics = new Statistics();
        int gamesPlayed = 0;
        int winsPlayerBlue = 0;
        int winsPlayerRed = 0;
        long startTime = System.currentTimeMillis();
        //for (int i = 0; i < NUM_GAMES; i++) {
        while (System.currentTimeMillis() - startTime < TIME) {
            gamesPlayed++;
            game = new Game(); // Create a new game instance
            game.start(); // Start the game
            if (game.getStatistics().getWinner().getColor() == Color.BLUE) {
                winsPlayerBlue++;
            } else {
                winsPlayerRed++;
            }
            statistics.setPossibleMoves(((statistics.getPossibleMoves() * statistics.getTotalMoves())
                    + (game.getStatistics().getPossibleMoves() * game.getStatistics().getTotalMoves()))
                    / (statistics.getTotalMoves() + game.getStatistics().getTotalMoves()));
            statistics.setTotalMoves(statistics.getTotalMoves() + game.getStatistics().getTotalMoves());
            statistics.setTotalTurns(statistics.getTotalTurns() + game.getStatistics().getTotalTurns());
            statistics.setBlueMasterMoves(statistics.getBlueMasterMoves() + game.getStatistics().getBlueMasterMoves());
            statistics.setRedMasterMoves(statistics.getRedMasterMoves() + game.getStatistics().getRedMasterMoves());
            statistics.setCapturedBluePieces(statistics.getCapturedBluePieces() + game.getStatistics().getCapturedBluePieces());
            statistics.setCapturedRedPieces(statistics.getCapturedRedPieces() + game.getStatistics().getCapturedRedPieces());
            statistics.setBlueMasterCaptures(statistics.getBlueMasterCaptures() + game.getStatistics().getBlueMasterCaptures());
            statistics.setRedMasterCaptures(statistics.getRedMasterCaptures() + game.getStatistics().getRedMasterCaptures());
            statistics.setBlueTempleReached(statistics.getBlueTempleReached() + game.getStatistics().getBlueTempleReached());
            statistics.setRedTempleReached(statistics.getRedTempleReached() + game.getStatistics().getRedTempleReached());
            statistics.setStartingPlayerWins(statistics.getStartingPlayerWins() + game.getStatistics().getStartingPlayerWins());
            statistics.setStartingPlayerLosses(statistics.getStartingPlayerLosses() + game.getStatistics().getStartingPlayerLosses());
        }
        System.out.println("Simulation results (Games played: " + gamesPlayed + "):");
        System.out.printf("Average number of possible moves:        %.2f\n",
                statistics.getPossibleMoves());
        // System.out.println("BLUE C value:                            0.25          |                RED C value: 0.5");
        System.out.printf("BLUE wins:                               %d (%.2f%%)  | RED wins:                     %d (%.2f%%)\n",
                winsPlayerBlue, (double) winsPlayerBlue / gamesPlayed * 100,
                winsPlayerRed, (double) winsPlayerRed / gamesPlayed * 100);
        System.out.printf("Average number of blue master moves:     %.2f         | Average number of red master moves:       %.2f\n",
                (double) statistics.getBlueMasterMoves() / gamesPlayed,
                (double) statistics.getRedMasterMoves() / gamesPlayed);
        System.out.printf("Average number of red students captures: %.2f         | Average number of blue students captures: %.2f\n",
                ((double) statistics.getCapturedRedPieces() - statistics.getRedMasterCaptures()) / gamesPlayed,
                ((double) statistics.getCapturedBluePieces() - statistics.getBlueMasterCaptures()) / gamesPlayed);
        System.out.printf("Total number of red master captures:     %d (%.2f%%)  | Total number of blue master captures:     %d (%.2f%%)\n",
                statistics.getRedMasterCaptures(),
                (double) statistics.getRedMasterCaptures() / gamesPlayed * 100,
                statistics.getBlueMasterCaptures(),
                (double) statistics.getBlueMasterCaptures() / gamesPlayed * 100);
        System.out.printf("Total number of red temple reaches:      %d (%.2f%%)  | Total number of blue temple reaches:      %d (%.2f%%)\n",
                statistics.getRedTempleReached(),
                (double) statistics.getRedTempleReached() / gamesPlayed * 100,
                statistics.getBlueTempleReached(),
                (double) statistics.getBlueTempleReached() / gamesPlayed * 100);
        System.out.printf("Average number of moves:                %.2f         | Average number of turns:                 %.2f\n",
                (double) statistics.getTotalMoves() / gamesPlayed,
                (double) statistics.getTotalTurns() / gamesPlayed);
        System.out.printf("Total starting player wins:              %d (%.2f%%)  | Total starting player losses:            %d (%.2f%%)\n",
                statistics.getStartingPlayerWins(),
                (double) statistics.getStartingPlayerWins() / gamesPlayed * 100,
                statistics.getStartingPlayerLosses(),
                (double) statistics.getStartingPlayerLosses() / gamesPlayed * 100);

    }
}
