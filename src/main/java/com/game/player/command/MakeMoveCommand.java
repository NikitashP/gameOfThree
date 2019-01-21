package com.game.player.command;

public class MakeMoveCommand extends BaseCommand<String> {

    private final double value;

    public MakeMoveCommand(String scoreId, double value) {
        super(scoreId);
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
