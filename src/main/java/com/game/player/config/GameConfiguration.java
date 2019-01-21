package com.game.player.config;

import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfiguration {

    @Bean
    public InMemoryEventStorageEngine eventStore() {
        return new InMemoryEventStorageEngine();
    }
}
