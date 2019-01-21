package com.game.player.aggregate;

import java.util.ArrayList;
import java.util.List;

public class AggregateTracker {

    private List<String> tracker = new ArrayList();

    public AggregateTracker() {
    }

    public boolean track(String id) {
        return tracker.add(id);
    }

    public boolean contains(String id) {
        return tracker.contains(id);
    }
}
