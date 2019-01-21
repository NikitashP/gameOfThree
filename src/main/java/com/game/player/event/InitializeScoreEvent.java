package com.game.player.event;

public class InitializeScoreEvent extends BaseEvent<String> {

    private final long initialValue;

    public InitializeScoreEvent(String id, long initialValue) {
        super(id);
        this.initialValue = initialValue;
    }

    public long getInitialValue() {
        return initialValue;
    }
}
