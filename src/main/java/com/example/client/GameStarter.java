package com.example.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.logic.Color;
import com.example.logic.Game;
import com.example.logic.Move;
import com.example.logic.Piece;
import com.example.logic.Tile;
import com.example.players.MCTSheavyPlayer;
import com.example.players.MCTSlightPlayer;

public class GameStarter {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/konradGames";
    private boolean gameIsRunning;
    private Game game;
    private String gameId;
    private Color playerColor;
    private String KonradAiType;
    private String PhilAiType;

    public GameStarter() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        this.gameIsRunning = false;
        this.playerColor = Color.BLUE;
    }

    public static void main(String[] args) {
        GameStarter gameStarter = new GameStarter();

        gameStarter.runGames(1 * 60 * 1000, "MCTS", "light");
        gameStarter.runGames(1 * 60 * 1000, "RAVE_MCTS", "heavy");
        gameStarter.runGames(1 * 60 * 1000, "HEURISTIC_MCTS", "heavy");
    }

    public void test() {
        String url = baseUrl + "/test";
        String response = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(response);
    }

    public KonradResultObject runGame(String PhilAiType, String KonradAiType) {
        this.PhilAiType = PhilAiType;
        this.KonradAiType = KonradAiType;
        initializeGame();
        return run();
    }

    public void runGames(long duration, String PhilAiType, String KonradAiType) {
        double gameDurations = 0;
        double templeWins = 0;
        int abortedMatches = 0;
        int redwins = 0;
        int bluewins = 0;
        int overallMatches = 0;

        long startTime = System.currentTimeMillis();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("playoutsKonrad.txt", true))) {
            writer.write("Starting with agents: " + PhilAiType + " and " + KonradAiType
                    + duration / 1000 / 60 + "min");
            writer.newLine();
            // while (System.currentTimeMillis() - startTime < duration) {
            while (System.currentTimeMillis() - startTime < duration) {
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                Future<KonradResultObject> future = executorService.submit(() -> runGame(PhilAiType, KonradAiType));

                try {
                    // Wait for the AI vs AI match to complete, with a timeout of 10 minutes
                    KonradResultObject stats = future.get(10, TimeUnit.MINUTES);
                    if (stats == null) {
                        continue;
                    }
                    double gameDuration = Math.floor(stats.getDuration() / 1000);
                    gameDurations += gameDuration;
                    if (stats.isTempleWin()) {
                        templeWins++;
                    }

                    String result = "Game duration: " + gameDuration + "s" + "(" + gameDuration / 60
                            + "min)" + ", Temple win: " + stats.isTempleWin();

                    writer.write(result);
                    writer.newLine();
                    System.out.println(stats.getWinner());
                    if (stats.getWinner().equals("RED")) {
                        redwins++;
                    } else {
                        bluewins++;
                    }
                    overallMatches++;

                    String summary = "Red wins: " + redwins + ", Blue wins: " + bluewins + ", Avg Duration: "
                            + gameDurations / overallMatches
                            + ", Avg. Temple Wins: " + templeWins / overallMatches
                            + ", Aborted Matches: " + abortedMatches;
                    System.out.println(summary);
                    writer.write(summary);
                    writer.newLine();
                    writer.newLine();
                } catch (TimeoutException e) {
                    // Match took too long, so we cancel the task and move to the next one
                    future.cancel(true);
                    abortedMatches++;
                    overallMatches++;
                    writer.write("A match took too long and was aborted.");
                    writer.newLine();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    executorService.shutdown();
                }

            }

            writer.write("Red wins: " + redwins + ", Blue wins: " + bluewins);
            writer.newLine();
            writer.write(
                    "-----------------------------------------------------------------------------------------------------------------");
            writer.newLine();
            writer.write(
                    "-----------------------------------------------------------------------------------------------------------------");
            writer.newLine();
            writer.write(
                    "-----------------------------------------------------------------------------------------------------------------");
            writer.newLine();
            writer.write(
                    "-----------------------------------------------------------------------------------------------------------------");
            writer.newLine();
            writer.write(
                    "-----------------------------------------------------------------------------------------------------------------");
            writer.newLine();
            writer.write(
                    "-----------------------------------------------------------------------------------------------------------------");
            writer.newLine();
            writer.write(
                    "-----------------------------------------------------------------------------------------------------------------");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeGame() {
        // http://<your-server-address>/createGameKonrad?cards=card1&cards=card2&cards=card3&cards=card4&cards=card5
        this.game = new Game();
        StringBuilder cards = new StringBuilder();
        for (int i = 0; i < game.getPlayerBlue().getCards().size(); i++) {
            cards.append("cards=").append(game.getPlayerBlue().getCards().get(i).getName()).append("&");
        }

        for (int i = 0; i < game.getPlayerRed().getCards().size(); i++) {
            cards.append("cards=").append(game.getPlayerRed().getCards().get(i).getName()).append("&");
        }

        cards.append("cards=").append(game.getNeutralCard().getName());

        String url = baseUrl + "/create?" + cards.toString() + "&aiType=" + PhilAiType;

        System.out.println("url: " + url);

        String gameId = restTemplate.postForObject(url, null, String.class);
        this.gameId = gameId;

        System.out.println("Game ID: " + gameId);
        gameIsRunning = true;
    }

    public KonradResultObject run() {
        while (gameIsRunning) {
            try {
                if (isGameFinished()) {
                    gameIsRunning = false;
                    System.out.println("Game is finished.");
                    KonradResultObject result = restTemplate.getForObject(baseUrl + "/" + gameId + "/getResult",
                            KonradResultObject.class);
                    return result;
                }

                if (checkIfMyTurn()) {
                    KonradMoveObject state = getLatestMove();
                    if (state == null) {
                        Move konradsMove;
                        if (KonradAiType == "light") {
                            konradsMove = new MCTSlightPlayer("BLUE", Color.BLUE).move(game);
                        } else {
                            konradsMove = new MCTSheavyPlayer("BLUE", Color.BLUE).move(game);
                        }
                        game.playTurn(konradsMove);
                        System.out.println(game.getBoard());
                        // Move als KonradStateObject speichern
                        KonradMoveObject move = new KonradMoveObject(konradsMove.getOrigin().getX(),
                                konradsMove.getOrigin().getY(), konradsMove.getMovement()[0],
                                konradsMove.getMovement()[1], konradsMove.getCard().getName());
                        submitMove(move);
                    } else {
                        System.out.println(state.getX() + " " + state.getY() + " " + state.getMovementX() + " "
                                + state.getMovementY() + " " + state.getCardName());
                        // Hier mit dem State deinen Gamestate updaten
                        Piece piece = game.getBoard().getTile(state.getX(), state.getY()).getPiece();
                        Tile origin = piece.getTile();
                        Tile target = game.getBoard().getTile(origin.getX() + state.getMovementX(),
                                origin.getY() + state.getMovementY());
                        Move philsMove = new Move(game.getCardByName(state.getCardName()),
                                piece, new int[] { state.getMovementX(), state.getMovementY() },
                                origin, target);
                        game.playTurn(philsMove);
                        // Dann Move bauen
                        Move konradsMove;
                        if (KonradAiType == "light") {
                            konradsMove = new MCTSlightPlayer("BLUE", Color.BLUE).move(game);
                        } else {
                            konradsMove = new MCTSheavyPlayer("BLUE", Color.BLUE).move(game);
                        }
                        game.playTurn(konradsMove);
                        System.out.println(game.getBoard());
                        // Move als KonradStateObject speichern
                        KonradMoveObject move = new KonradMoveObject(konradsMove.getOrigin().getX(),
                                konradsMove.getOrigin().getY(), konradsMove.getMovement()[0],
                                konradsMove.getMovement()[1], konradsMove.getCard().getName());
                        submitMove(move);
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public KonradMoveObject getLatestMove() {
        String url = baseUrl + "/" + gameId + "/getLatestMove";
        KonradMoveObject konradStateObject = null;
        try {
            konradStateObject = restTemplate.getForObject(url, KonradMoveObject.class);
        } catch (HttpClientErrorException.BadRequest e) {
            System.out.println("No moves have been made yet.");
        }
        return konradStateObject;
    }

    public boolean checkIfMyTurn() {
        System.out.println(gameId);
        String url = baseUrl + "/" + gameId + "/isMyTurn/" + playerColor;

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Boolean.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            boolean isMyTurn = response.getBody();
            if (isMyTurn) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Failed to check if it's your turn.");
            return false;
        }
    }

    public boolean isGameFinished() {
        String url = baseUrl + "/" + gameId + "/isFinished";

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Boolean.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            boolean isFinished = response.getBody();
            if (isFinished) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Failed to check if game is finished.");
            return false;
        }
    }

    public boolean submitMove(KonradMoveObject move) {
        String url = baseUrl + "/" + gameId + "/submitMove?playerColor=" + playerColor;
        restTemplate.postForObject(url, move, String.class);
        return true;
    }

}
