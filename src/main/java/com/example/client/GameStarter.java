package com.example.client;

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
import com.example.players.MCTSLPPlayer;

public class GameStarter {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/konradGames";
    private boolean gameIsRunning;
    private Game game;
    private String gameId;
    private Color playerColor;

    public GameStarter() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        this.gameIsRunning = false;
        this.game = new Game();
        this.playerColor = Color.BLUE;
    }

    public static void main(String[] args) {
        GameStarter gameStarter = new GameStarter();
        // gameStarter.test();
        gameStarter.initializeGame();
        gameStarter.run();
    }

    public void test() {
        String url = baseUrl + "/test";
        String response = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(response);
    }

    public void initializeGame() {
        // http://<your-server-address>/createGameKonrad?cards=card1&cards=card2&cards=card3&cards=card4&cards=card5
        StringBuilder cards = new StringBuilder();
        for (int i = 0; i < game.getPlayerBlue().getCards().size(); i++) {
            cards.append("cards=").append(game.getPlayerBlue().getCards().get(i).getName()).append("&");
        }

        for (int i = 0; i < game.getPlayerRed().getCards().size(); i++) {
            cards.append("cards=").append(game.getPlayerRed().getCards().get(i).getName()).append("&");
        }

        cards.append("cards=").append(game.getNeutralCard().getName());

        String url = baseUrl + "/create?" + cards.toString();

        System.out.println("url: " + url);

        String gameId = restTemplate.postForObject(url, null, String.class);
        this.gameId = gameId;

        System.out.println("Game ID: " + gameId);
        gameIsRunning = true;
    }

    public void run() {
        while (gameIsRunning) {
            try {
                if (isGameFinished()) {
                    gameIsRunning = false;
                    System.out.println("Game is finished.");
                    break;
                }

                if (checkIfMyTurn()) {
                    KonradMoveObject state = getLatestMove();
                    if (state == null) {
                        Move konradsMove = new MCTSLPPlayer("BLUE", Color.BLUE).move(game);
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
                        Move konradsMove = new MCTSLPPlayer("BLUE", Color.BLUE).move(game);
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
