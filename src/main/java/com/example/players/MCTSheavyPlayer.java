package com.example.players;

import java.util.ArrayList;
import java.util.List;

import com.example.logic.Board;
import com.example.logic.Card;
import com.example.logic.Color;
import com.example.logic.Game;
import com.example.logic.Move;
import com.example.logic.Piece;
import com.example.logic.Tile;

public class MCTSheavyPlayer extends Player {

    private static final int TIMELIMIT = 2000;  // Time limit in milliseconds (2 seconds)
    private static final int MAX_DEPTH = 50;  // Maximum depth for the MCTS tree

    public MCTSheavyPlayer(String name, Color color) {
        super(name, color);
    }

    @Override
    public Move move(Game game) {
        NodeHeavy rootNode = new NodeHeavy(game.clone());  // Create a root node with the current game state, no move or parent

        //int iterations = 0;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < TIMELIMIT) {
            NodeHeavy selectedNode = select(rootNode);
            NodeHeavy child = expand(selectedNode);
            double result = simulate(child);
            backpropagate(child, result);
            // iterations++;
        }

        // System.out.println("Iterations (heavy): " + iterations);
        // Step 5: Return the move from the most visited child node
        return getBestMove(rootNode).getEquivalentMove(game);
    }

    // Step 1: Selection - Traverse the tree using UCT to find the best node to explore
    private NodeHeavy select(NodeHeavy node) {
        while (!node.children.isEmpty() && node.children.size() == Board.getPossibleMoves(node.game).size()) {
            node = bestUCT(node);
        }
        return node;
    }

    // Step 2: Expansion - Add a new child node for each possible move
    private NodeHeavy expand(NodeHeavy node) {
        if (node.game.isGameOver()) {
            return node;
        }
        List<Move> possibleMoves = Board.getPossibleMoves(node.game);
        List<Move> usedMoves = new ArrayList<>();
        for (NodeHeavy child : node.children) {
            Move move = child.move;
            Piece piece = move.getPiece().clone();
            piece.setTile(move.getOrigin());
            usedMoves.add(new Move(move.getCard(), piece, move.getMovementCopy(), move.getOrigin(), move.getTarget()));
        }
        possibleMoves.removeAll(usedMoves);
        Move move = possibleMoves.get((int) (Math.random() * possibleMoves.size()));
        Game newGameState = node.game.clone();
        Card card = move.getCard().clone();
        // Map the original objects (piece, Card) in the move to the cloned game objects
        Piece piece = move.getEquivalentPiece(newGameState);
        Tile originTile = piece.getTile(); // This should represent the origin tile
        Tile targetTile = newGameState.getBoard().getTile(
                originTile.getX() + move.getMovement()[0],
                originTile.getY() + move.getMovement()[1]
        );

        // Create a new move using the mapped objects from the cloned game
        Move clonedMove = new Move(card, piece, move.getMovementCopy(), originTile, targetTile);

        // Now apply the cloned move to the cloned game
        newGameState.playTurn(clonedMove);

        // Create a child node with the cloned game state
        NodeHeavy childNode = new NodeHeavy(newGameState, clonedMove, node);
        node.addChild(childNode);  // Add the new child node to the parent's children list
        return childNode;
    }

    // Step 3: Simulation - Play a random game to the end from this state and return the result
    private double simulate(NodeHeavy node) {
        if (node.game.isGameOver()) {
            return 1;
        }
        Game tempGame = node.game.clone();
        Color currentPlayerColor = tempGame.getCurrentPlayer().getColor();
        Color previousPlayerColor = currentPlayerColor == Color.BLUE ? Color.RED : Color.BLUE;

        int depth = 0;

        while (!tempGame.isGameOver() && depth < MAX_DEPTH) {
            List<Move> possibleMoves = Board.getPossibleMoves(tempGame);
            // Check if there are no possible moves
            if (possibleMoves.isEmpty()) {
                // Skip the turn if there are no possible moves (this is done by passing null)
                tempGame.playTurn(null);
                // Continue the simulation
                continue;
            }
            Player currentPlayer = tempGame.getCurrentPlayer();
            Move aggressiveMove = new AggressivePlayer(currentPlayer.getName(),
                    currentPlayer.getColor()).move(tempGame);
            tempGame.playTurn(aggressiveMove);
            depth++;
        }

        // If maximum depth is reached without a clear game-over state, return a heuristic evaluation
        if (depth >= MAX_DEPTH) {
            return evaluateGameState(tempGame, previousPlayerColor); // Use heuristic evaluation
        }

        // Winner of simulation = player at the parent node? If yes -> 1, else 0
        // return tempGame.getCurrentPlayer().getColor() == previousPlayerColor ? 1 : 0;  // 1 for win, 0 for loss
        return tempGame.getStatistics().getWinner().getColor() == previousPlayerColor ? 1 : 0;  // 1 for win, 0 for loss
    }

    private double evaluateGameState(Game game, Color playerColor) {
        Player currentPlayer = playerColor == Color.BLUE ? game.getPlayerBlue() : game.getPlayerRed();
        Player opponent = playerColor == Color.BLUE ? game.getPlayerRed() : game.getPlayerBlue();

        int pieceDifference = currentPlayer.getPieces().size() - opponent.getPieces().size();

        // Heuristic: Favor the player with more pieces
        if (pieceDifference > 0) {
            return 1; // Favor current player
        } else if (pieceDifference < 0) {
            return 0; // Favor opponent
        }

        // Neutral heuristic in case of tie
        return 0.5; // Can be adjusted depending on your algorithm's requirements
    }

    // Step 4: Backpropagation - Update the current node and all ancestors with the result
    private void backpropagate(NodeHeavy node, double result) {
        while (node != null) {
            node.visits++;
            node.score += result;  // Adjust based on the result of the simulation
            node = node.parent;
            result = 1 - result;  // Switch the result for the parent node
        }
    }

    // Helper function to get the best child based on UCT
    private NodeHeavy bestUCT(NodeHeavy node) {
        NodeHeavy bestChild = null;
        double bestUCTValue = Double.NEGATIVE_INFINITY;

        for (NodeHeavy child : node.children) {
            double uctValue = child.getUCTValue(this.getColor());
            if (uctValue > bestUCTValue) {
                bestUCTValue = uctValue;
                bestChild = child;
                if (bestUCTValue == Double.MAX_VALUE) {
                    break;
                }
            }
        }
        return bestChild;
    }

    // Step 5: Choose the best move based on the most visited child node
    private Move getBestMove(NodeHeavy rootNode) {
        NodeHeavy bestNode = null;
        int mostVisits = Integer.MIN_VALUE;

        for (NodeHeavy child : rootNode.children) {
            if (child.visits > mostVisits) {
                mostVisits = child.visits;
                bestNode = child;
            }
        }
        // Get the move from the best node (which is stored in the node itself)
        if (bestNode != null) {
            return bestNode.move;
        }
        return null;
    }

    @Override
    public Player clone() {
        return new MCTSheavyPlayer(this.getName(), this.getColor());
    }
}

class NodeHeavy {

    Game game;  // Der aktuelle Zustand des Spiels in diesem Knoten
    NodeHeavy parent;  // Der Elternknoten
    List<NodeHeavy> children;  // Die Kindknoten
    int visits = 0;  // Anzahl der Besuche dieses Knotens (MCTS)
    double score = 0;  // Anzahl der Siege aus diesem Knoten
    Move move;  // Der Zug, der diesen Knoten erzeugt hat

    // Konstruktor, der den Zug zusätzlich aufnimmt
    NodeHeavy(Game game, Move move, NodeHeavy parent) {
        this.game = game;
        this.move = move;  // Speichern des Zugs, der zu diesem Knoten führt
        this.parent = parent;  // Setzt den Elternknoten
        this.children = new ArrayList<>();
    }

    // Konstruktor für den Wurzelknoten
    NodeHeavy(Game game) {
        this.game = game;
        this.move = null;  // Der Wurzelknoten hat keinen Zug
        this.parent = null;  // Der Wurzelknoten hat keinen Elternknoten
        this.children = new ArrayList<>();
    }

    // UCT-Wert (Upper Confidence Bound applied to Trees)
    double getUCTValue(Color color) {
        if (visits == 0) { // TODO: Check if this is correct
            // return Double.MAX_VALUE;  // Return max value if the node has not been visited yet
            return 0;
        }
        double exploitation = score / visits;

        double exploration = Math.sqrt(Math.log(parent.visits) / visits);

        double c = 0.25;

        return exploitation + c * exploration;  // 1.41 is sqrt(2)
    }

    // Methode zum Hinzufügen eines Kindknotens
    void addChild(NodeHeavy child) {
        this.children.add(child);
    }
}
