package com.example.logic;

import java.util.List;

import com.example.players.MCTSheavyPlayer;
import com.example.players.MCTSlightPlayer;
import com.example.players.Player;

/**
 * The Game class represents a game of Onitama. It manages the game board,
 * players, cards, and the game flow.
 */
public class Game {

    private Board board;                // The game board
    private Player playerBlue;             // Player 1
    private Player playerRed;             // Player 2
    private Player currentPlayer;       // The current player
    private Card neutralCard;           // The neutral card that switches after each turn
    private List<Card> usedCards;       // The cards that are used in the game
    private Statistics statistics;      // The game statistics
    private Player startingPlayer;      // The starting player
    private boolean gameOver;           // Flag to indicate if the game is over

    /**
     * Initializes a new instance of the Game class.
     */
    public Game() {
        initializePlayers();            // Initialize the players
        initializeBoard();              // Initialize the game board
        initializeCards();              // Initialize the cards
        initializeStatistics();         // Initialize the statistics
    }

    public Game(Game game) {
        this.board = game.board.clone();
        this.playerBlue = game.playerBlue.clone();
        this.playerRed = game.playerRed.clone();
        this.currentPlayer = game.currentPlayer == game.playerBlue ? this.playerBlue : this.playerRed;
        this.neutralCard = game.neutralCard.clone();
        this.statistics = game.statistics.clone();
        this.startingPlayer = game.startingPlayer == game.playerBlue ? this.playerBlue : this.playerRed;
        this.gameOver = game.gameOver;
        //if (this.gameOver) {
        this.statistics.setWinner(this.currentPlayer);
        this.statistics.setLoser((this.currentPlayer == this.playerBlue) ? this.playerRed : this.playerBlue);
        //}
    }

    /**
     * Initializes the players of the game.
     */
    private void initializePlayers() {
        playerBlue = new MCTSlightPlayer("BLUE", Color.BLUE);   // Create Player 1
        playerRed = new MCTSheavyPlayer("RED", Color.RED);     // Create Player 2
    }

    /**
     * Initializes the game board.
     */
    private void initializeBoard() {
        board = new Board();    // Create a new game board

        // Set up the initial positions of all pieces on the board
        board.initializeBoard(playerBlue, playerRed);
    }

    /**
     * Initializes the cards for each player and the neutral card.
     */
    private void initializeCards() {
        // Create all cards
        List<Card> allCards = Card.initializeCards();

        // Distribute two cards to each player and set one as the neutral card
        Card card1 = allCards.remove((int) (Math.random() * allCards.size()));
        Card card2 = allCards.remove((int) (Math.random() * allCards.size()));
        playerBlue.getCards().add(card1);
        playerBlue.getCards().add(card2);
        // player1.setCards(new ArrayList<>(Arrays.asList(card1, card2)));

        card1 = allCards.remove((int) (Math.random() * allCards.size()));
        card2 = allCards.remove((int) (Math.random() * allCards.size()));
        playerRed.getCards().add(card1);
        playerRed.getCards().add(card2);
        // player2.setCards(new ArrayList<>(Arrays.asList(card1, card2)));

        neutralCard = allCards.remove((int) (Math.random() * allCards.size()));
        startingPlayer = neutralCard.getColor() == Color.BLUE ? playerBlue : playerRed;
        currentPlayer = startingPlayer;

        usedCards = List.of(playerBlue.getCards().get(0), playerBlue.getCards().get(1),
                playerRed.getCards().get(0), playerRed.getCards().get(1), neutralCard);

        // System.out.println("Player 1 cards: " + player1.getCards());
        // System.out.println("Player 2 cards: " + player2.getCards());
        // System.out.println("Neutral card: " + neutralCard);
        // System.out.println("Starting player: " + currentPlayer.getName());
    }

    /**
     * Initializes the game statistics.
     */
    private void initializeStatistics() {
        statistics = new Statistics();    // Create a new game statistics
        statistics.initializeCardUsage(usedCards);
    }

    /**
     * Starts the game.
     *
     * @throws InterruptedException
     */
    public void start() {
        gameOver = false;
        // Main game loop or turn management goes here
        while (!checkGameOver()) {
            playTurn();
        }
    }

    /**
     * Plays a turn in the game.
     */
    private void playTurn() {
        System.out.print(this.board);
        // Print current player's turn
        System.out.println(currentPlayer.getName() + "'s turn.");

        this.statistics.increaseTotalMoves();

        if (currentPlayer == startingPlayer) {
            this.statistics.increaseTotalTurns();
        }

        // Get the possible moves
        List<Move> possibleMoves = Board.getPossibleMoves(this);

        this.statistics.setPossibleMoves((statistics.getPossibleMoves() * (statistics.getTotalMoves() - 1) + possibleMoves.size())
                / statistics.getTotalMoves());

        if (Board.getPossibleMoves(this).isEmpty()) {
            System.out.println("No moves available. Skipping turn.");
            Card randomCard = currentPlayer.getCards().get((int) (Math.random() * currentPlayer.getCards().size()));
            currentPlayer.getCards().remove(randomCard);
            currentPlayer.getCards().add(neutralCard);
            neutralCard = randomCard;
            switchPlayers();
            return;
        }
        // Get the current player's move
        Move move = currentPlayer.move(this);
        // for performance reasons
        // if (!possibleMoves.contains(move)) {
        //     System.out.println("Invalid move. Choosing random move.");
        //     move = possibleMoves.get((int) (Math.random() * possibleMoves.size()));
        //     //currentPlayer = (currentPlayer == player1) ? player2 : player1;
        //     //return;
        // }
        Card card = move.getCard();
        statistics.increaseCardUsage(card);
        Piece piece = move.getPiece();
        switch (piece.getType()) {
            case BLUEMASTER ->
                this.statistics.increaseBlueMasterMoves();
            case REDMASTER ->
                this.statistics.increaseRedMasterMoves();
            default -> {
            }
        }
        Tile origin = piece.getTile();
        int[] movement = move.getMovement();
        Tile target = board.getTile(origin.getX() + movement[0], origin.getY() + movement[1]);
        if (target.getPiece() != null) {
            // Capture piece
            Player enemyPlayer = (currentPlayer == playerBlue) ? playerRed : playerBlue;
            enemyPlayer.getPieces().remove(target.getPiece());
            //System.out.println("Piece captured: " + target.getPiece());
            switch (target.getPiece().getType()) {
                case BLUESTUDENT, BLUEMASTER ->
                    this.statistics.increaseCapturedBluePieces();
                case REDSTUDENT, REDMASTER ->
                    this.statistics.increaseCapturedRedPieces();
                default -> {
                }
            }
        }
        origin.setPiece(null);
        target.setPiece(piece);
        piece.setTile(target);

        currentPlayer.getCards().remove(card);
        currentPlayer.getCards().add(neutralCard);
        neutralCard = card;

        // System.out.println(move + " origin: " + origin.toString() + " target: " + target);
        // System.out.println(board);
        // Execute move
        if (checkGameOver()) {
            // System.out.println(this.board);
            gameOver = true;
            declareWinner();
            this.statistics.setWinner(currentPlayer);
            this.statistics.setLoser((currentPlayer == playerBlue) ? playerRed : playerBlue);
            this.statistics.setStartingPlayerWins((currentPlayer == startingPlayer) ? 1 : 0);
            this.statistics.setStartingPlayerLosses((currentPlayer == startingPlayer) ? 0 : 1);
            return;
        }

        // try {
        //     Thread.sleep(1000);
        // } catch (InterruptedException ex) {
        // }
        // Switch players
        switchPlayers();
    }

    public void playTurn(Move move) {
        //System.out.println("Simulation:");
        // Print current player's turn
        //System.out.println(currentPlayer.getName() + "'s turn.");

        // this.statistics.increaseTotalMoves();
        // if (currentPlayer == startingPlayer) {
        //     this.statistics.increaseTotalTurns();
        // }
        if (move == null) {
            System.out.println("No moves available. Skipping turn.");
            Card randomCard = currentPlayer.getCards().get((int) (Math.random() * currentPlayer.getCards().size()));
            currentPlayer.getCards().remove(randomCard);
            currentPlayer.getCards().add(neutralCard);
            neutralCard = randomCard;
            switchPlayers();
            return;
        }
        Card card = move.getCard();
        Piece piece = move.getPiece();
        Tile origin = piece.getTile();
        int[] movement = move.getMovement();
        Tile target = board.getTile(origin.getX() + movement[0], origin.getY() + movement[1]);
        if (target.getPiece() != null) {
            // Capture piece
            Player enemyPlayer = (currentPlayer == playerBlue) ? playerRed : playerBlue;
            enemyPlayer.getPieces().remove(target.getPiece());
        }
        origin.setPiece(null);
        target.setPiece(piece);
        piece.setTile(target);

        currentPlayer.getCards().remove(card);
        currentPlayer.getCards().add(neutralCard);
        neutralCard = card;

        if (checkGameOver()) {
            gameOver = true;
            // declareWinner();
            this.statistics.setWinner(currentPlayer);
            this.statistics.setLoser((currentPlayer == playerBlue) ? playerRed : playerBlue);
            //System.out.print(board);
            return;
        }

        // Switch players
        switchPlayers();
        //System.out.print(board);
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise.
     */
    private boolean checkGameOver() {
        // Implement game over logic based on win conditions
        for (Piece piece : currentPlayer.getPieces()) {
            if (piece.getType() == PieceType.BLUEMASTER && piece.getTile().isRedTemple()) {
                this.statistics.setRedTempleReached(1);
                return true;
            } else if (piece.getType() == PieceType.REDMASTER && piece.getTile().isBlueTemple()) {
                this.statistics.setBlueTempleReached(1);
                return true;
            }
        }

        Player enemyPlayer = (currentPlayer == playerBlue) ? playerRed : playerBlue;
        for (Piece piece : enemyPlayer.getPieces()) {
            if (piece.getType() == PieceType.BLUEMASTER || piece.getType() == PieceType.REDMASTER) {
                return false;
            }
        }
        switch (enemyPlayer.getColor()) {
            case BLUE -> {
                this.statistics.setBlueMasterCaptures(1);
            }
            case RED -> {
                this.statistics.setRedMasterCaptures(1);
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Declares the winner of the game.
     */
    private void declareWinner() {
        // Logic to declare the winner
        System.out.println("Game over. Winner: " + currentPlayer.getName());
    }

    /**
     * Clones the current game state.
     *
     * @return a clone of the current game state.
     */
    @Override
    public Game clone() {
        Game clone = new Game(this);
        clone.playerBlue.setCards(playerBlue.getCardsCopy());
        clone.playerRed.setCards(playerRed.getCardsCopy());
        for (Tile[] tile : clone.board.getTiles()) {
            for (Tile t : tile) {
                Piece piece = t.getPiece();
                if (piece == null) {
                    continue;
                }
                if (piece.getColor() == Color.BLUE) {
                    clone.playerBlue.getPieces().add(piece);
                } else {
                    clone.playerRed.getPieces().add(piece);
                }
            }
        }
        // clone.allCards = new ArrayList<>(allCards);
        clone.neutralCard = neutralCard.clone();
        return clone;
    }

    /**
     * Gets the current player.
     *
     * @return the current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the game board.
     *
     * @return the game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the game statistics.
     *
     * @return the game statistics.
     */
    public Statistics getStatistics() {
        return this.statistics;
    }

    /**
     * Gets the first player.
     *
     * @return the first player.
     */
    public Player getPlayerBlue() {
        return playerBlue;
    }

    /**
     * Gets the second player.
     *
     * @return the second player.
     */
    public Player getPlayerRed() {
        return playerRed;
    }

    public Card getNeutralCard() {
        return neutralCard;
    }

    public Card getCardByName(String name) {
        for (Card card : usedCards) {
            if (card.getName().equals(name)) {
                return card;
            }
        }
        return null;
    }

    private void switchPlayers() {
        currentPlayer = (currentPlayer == playerBlue) ? playerRed : playerBlue;
    }

}
