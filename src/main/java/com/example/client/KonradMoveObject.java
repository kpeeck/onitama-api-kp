package com.example.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KonradMoveObject {
    @JsonProperty("x")
    private int x;

    @JsonProperty("y")
    private int y;

    @JsonProperty("movementX")
    private int movementX;

    @JsonProperty("movementY")
    private int movementY;

    @JsonProperty("cardName")
    private String cardName;

    public KonradMoveObject() {
    }

    public KonradMoveObject(int x, int y, int movementX, int movementY, String cardName) {
        this.x = x;
        this.y = y;
        this.movementX = movementX;
        this.movementY = movementY;
        this.cardName = cardName;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getMovementX() {
        return movementX;
    }

    public void setMovementX(int movementX) {
        this.movementX = movementX;
    }

    public int getMovementY() {
        return movementY;
    }

    public void setMovementY(int movementY) {
        this.movementY = movementY;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}
