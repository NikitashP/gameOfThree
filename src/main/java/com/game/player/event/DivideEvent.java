package com.game.player.event;

public class DivideEvent extends BaseEvent<String> {

    private int divisor;

    public DivideEvent(String id, int divisor) {
        super(id);
        this.divisor = divisor;
    }

    public int getDivisor() {
        return divisor;
    }
}
