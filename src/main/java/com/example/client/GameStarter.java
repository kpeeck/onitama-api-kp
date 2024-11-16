package com.example.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.logic.Color;
import com.example.logic.Game;

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
        StringBuffer cards = new StringBuffer();
        for (int i = 0; i < game.getPlayerBlue().getCards().size(); i++) {
            cards.append("cards=" + game.getPlayerBlue().getCards().get(i).getName() + "&");
        }

        for (int i = 0; i < game.getPlayerRed().getCards().size(); i++) {
            cards.append("cards=" + game.getPlayerRed().getCards().get(i).getName() + "&");
        }

        cards.append("cards=" + game.getNeutralCard().getName());

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
                        System.out.println("No moves have been made yet.");
                    } else {
                        // Hier mit dem State deinen Gamestate updaten
                        // Dann Move bauen
                        // Move als KonradStateObject speichern
                        System.out.println(state.getX() + " " + state.getY() + " " + state.getMovementX() + " "
                                + state.getMovementY() + " " + state.getCardName());
                        KonradMoveObject move = new KonradMoveObject();
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
