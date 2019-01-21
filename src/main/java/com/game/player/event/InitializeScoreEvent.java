package com.game.player.event;

public class InitializeScoreEvent extends BaseEvent<String> {

    private final double initialValue;

    public InitializeScoreEvent(String id, double initialValue) {
        super(id);
        this.initialValue = initialValue;
    }

    public double getInitialValue() {
        return initialValue;
    }
}
