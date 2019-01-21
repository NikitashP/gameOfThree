package com.game.player.command;

public class StartGameCommand extends BaseCommand<String> {

    private final double initialValue;

    public StartGameCommand(String id, double initialValue) {
        super(id);
        this.initialValue = initialValue;
    }

    public double getInitialValue() {
        return initialValue;
    }
}
