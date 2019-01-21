package com.game.player.event;

import java.time.Instant;

import org.springframework.util.Assert;

public class BaseEvent<T> {

    public final T id;

    public Instant instant;

    public BaseEvent(T id) {
        Assert.notNull(id, "Id cannot be null");
        this.id = id;
        instant = instant.now();
    }
}
