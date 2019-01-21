package com.game.player.event;

public class ResetScoreEvent extends BaseEvent<String> {

    private final long updatedValue;

    public ResetScoreEvent(String id, long updatedValue) {
        super(id);
        this.updatedValue = updatedValue;
    }

    public long getUpdatedValue() {
        return updatedValue;
    }
}
