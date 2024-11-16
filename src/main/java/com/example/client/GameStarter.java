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
    private final String baseUrl = "http://localhost:8080/game";
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

    public void run() {

        while (gameIsRunning) {
            if (true) {
                KonradStateObject state = getLatestMove();
                if (state == null) {
                    System.out.println("No moves have been made yet.");
                }
            } else {

            }
        }

    }

    public KonradStateObject getLatestMove() {
        String url = baseUrl + "/" + gameId + "/getLatestMove";
        KonradStateObject konradStateObject = null;
        try {
            konradStateObject = restTemplate.getForObject(url, KonradStateObject.class);
        } catch (HttpClientErrorException.BadRequest e) {
            System.out.println("No moves have been made yet.");
        }
        return konradStateObject;
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

        String url = baseUrl + "/createGameKonrad?" + cards.toString();

        System.out.println("url: " + url);

        String gameId = restTemplate.postForObject(url, null, String.class);
        this.gameId = gameId;

        System.out.println("Game ID: " + gameId);
        gameIsRunning = true;
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

    public void test() {
        String url = baseUrl + "/test?test2=hello";
        String test = restTemplate.postForObject(url, null, String.class);
        System.out.println(test);
    }

    public static void main(String[] args) {
        GameStarter gameStarter = new GameStarter();
        gameStarter.initializeGame();
        gameStarter.run();
    }

}
