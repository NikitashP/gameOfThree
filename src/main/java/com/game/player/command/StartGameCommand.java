package com.game.player.command;

public class StartGameCommand extends BaseCommand<String> {

    private final long initialValue;

    public StartGameCommand(String id, long initialValue) {
        super(id);
        this.initialValue = initialValue;
    }

    public long getInitialValue() {
        return initialValue;
    }
}
