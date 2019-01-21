package com.game.player.event;

public class ResetScoreEvent extends BaseEvent<String> {

    private final double updatedValue;

    public ResetScoreEvent(String id, double updatedValue) {
        super(id);
        this.updatedValue = updatedValue;
    }

    public double getUpdatedValue() {
        return updatedValue;
    }
}
