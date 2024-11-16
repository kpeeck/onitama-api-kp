package com.example.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.players.Player;

public class Statistics {

    private Player winner;
    private Player loser;
    private int totalMoves;
    private int totalTurns;
    private int blueMasterMoves;
    private int redMasterMoves;
    private int capturedBluePieces;
    private int capturedRedPieces;
    private int blueMasterCaptures;
    private int redMasterCaptures;
    private int blueTempleReached;
    private int redTempleReached;
    private int startingPlayerWins;
    private int startingPlayerLosses;
    private double possibleMoves;
    private Map<Card, Integer> cardUsage;

    public Statistics() {
        this.winner = null;
        this.loser = null;
        this.totalMoves = 0;
        this.totalTurns = 0;
        this.blueMasterMoves = 0;
        this.redMasterMoves = 0;
        this.capturedBluePieces = 0;
        this.capturedRedPieces = 0;
        this.blueMasterCaptures = 0;
        this.redMasterCaptures = 0;
        this.blueTempleReached = 0;
        this.redTempleReached = 0;
        this.startingPlayerWins = 0;
        this.startingPlayerLosses = 0;
        this.possibleMoves = 0;
        this.cardUsage = new HashMap<>();
    }

    @Override
    public Statistics clone() {
        Statistics clone = new Statistics();
        clone.setTotalMoves(this.totalMoves);
        clone.setTotalTurns(this.totalTurns);
        clone.setBlueMasterMoves(this.blueMasterMoves);
        clone.setRedMasterMoves(this.redMasterMoves);
        clone.setCapturedBluePieces(this.capturedBluePieces);
        clone.setCapturedRedPieces(this.capturedRedPieces);
        clone.setBlueMasterCaptures(this.blueMasterCaptures);
        clone.setRedMasterCaptures(this.redMasterCaptures);
        clone.setBlueTempleReached(this.blueTempleReached);
        clone.setRedTempleReached(this.redTempleReached);
        clone.setStartingPlayerWins(this.startingPlayerWins);
        clone.setStartingPlayerLosses(this.startingPlayerLosses);
        clone.setPossibleMoves(this.possibleMoves);
        clone.setCardUsage(new HashMap<>(this.cardUsage));
        return clone;
    }

    // Initialize the card usage map
    public void initializeCardUsage(List<Card> cards) {
        for (Card card : cards) {
            cardUsage.put(card, 0);  // Start with 0 uses for each card
        }
    }

    // Increase the count for a card when it's used
    public void increaseCardUsage(Card card) {
        cardUsage.put(card, cardUsage.get(card) + 1);
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getLoser() {
        return loser;
    }

    public void setLoser(Player loser) {
        this.loser = loser;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public void increaseTotalMoves() {
        this.totalMoves += 1;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public void setTotalTurns(int totalTurns) {
        this.totalTurns = totalTurns;
    }

    public void increaseTotalTurns() {
        this.totalTurns += 1;
    }

    public int getBlueMasterMoves() {
        return blueMasterMoves;
    }

    public void setBlueMasterMoves(int blueMasterMoves) {
        this.blueMasterMoves = blueMasterMoves;
    }

    public void increaseBlueMasterMoves() {
        this.blueMasterMoves += 1;
    }

    public int getRedMasterMoves() {
        return redMasterMoves;
    }

    public void setRedMasterMoves(int redMasterMoves) {
        this.redMasterMoves = redMasterMoves;
    }

    public void increaseRedMasterMoves() {
        this.redMasterMoves += 1;
    }

    public int getCapturedBluePieces() {
        return capturedBluePieces;
    }

    public void setCapturedBluePieces(int capturedBluePieces) {
        this.capturedBluePieces = capturedBluePieces;
    }

    public void increaseCapturedBluePieces() {
        this.capturedBluePieces += 1;
    }

    public int getCapturedRedPieces() {
        return capturedRedPieces;
    }

    public void setCapturedRedPieces(int capturedRedPieces) {
        this.capturedRedPieces = capturedRedPieces;
    }

    public void increaseCapturedRedPieces() {
        this.capturedRedPieces += 1;
    }

    public int getBlueMasterCaptures() {
        return blueMasterCaptures;
    }

    public void setBlueMasterCaptures(int blueMasterCaptures) {
        this.blueMasterCaptures = blueMasterCaptures;
    }

    public int getRedMasterCaptures() {
        return redMasterCaptures;
    }

    public void setRedMasterCaptures(int redMasterCaptures) {
        this.redMasterCaptures = redMasterCaptures;
    }

    public int getBlueTempleReached() {
        return blueTempleReached;
    }

    public void setBlueTempleReached(int blueTempleReached) {
        this.blueTempleReached = blueTempleReached;
    }

    public int getRedTempleReached() {
        return redTempleReached;
    }

    public void setRedTempleReached(int redTempleReached) {
        this.redTempleReached = redTempleReached;
    }

    public int getStartingPlayerWins() {
        return startingPlayerWins;
    }

    public void setStartingPlayerWins(int startingPlayerWins) {
        this.startingPlayerWins = startingPlayerWins;
    }

    public int getStartingPlayerLosses() {
        return startingPlayerLosses;
    }

    public void setStartingPlayerLosses(int startingPlayerLosses) {
        this.startingPlayerLosses = startingPlayerLosses;
    }

    public double getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(double possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    // Get the usage count for a specific card
    public int getCardUsage(Card card) {
        return cardUsage.getOrDefault(card, 0);
    }

    // Get all card usage statistics
    public Map<Card, Integer> getCardUsageStats() {
        return cardUsage;
    }

    public void setCardUsage(HashMap<Card, Integer> hashMap) {
        this.cardUsage = hashMap;
    }

}
