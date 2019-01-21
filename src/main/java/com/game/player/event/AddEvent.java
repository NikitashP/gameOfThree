package com.game.player.event;

public class AddEvent extends BaseEvent<String> {

    private final int toAdd;

    public AddEvent(String id, int toAdd) {
        super(id);
        this.toAdd = toAdd;
    }

    public int getToAdd() {
        return toAdd;
    }
}
