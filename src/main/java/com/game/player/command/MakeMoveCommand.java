package com.game.player.command;

public class MakeMoveCommand extends BaseCommand<String> {

    private final long value;

    public MakeMoveCommand(String scoreId, long value) {
        super(scoreId);
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
