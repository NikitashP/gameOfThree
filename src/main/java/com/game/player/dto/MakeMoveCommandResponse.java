package com.game.player.dto;

public class MakeMoveCommandResponse {

    private final String id;

    private final double value;

    public MakeMoveCommandResponse(String id, double value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public double getValue() {
        return value;
    }
}
