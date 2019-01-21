package com.game.player.config;

import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.spring.commandhandling.gateway.CommandGatewayFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.game.player.aggregate.AggregateTracker;

@Configuration
public class GameConfiguration {

    @Bean
    public InMemoryEventStorageEngine eventStore() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public AggregateTracker aggregateTracker() {
        return new AggregateTracker();
    }

    @Bean
    public CommandBus commandBus() {
        SimpleCommandBus commandBus = new AsynchronousCommandBus();
        return commandBus;
    }

    @Bean
    public CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean() {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
        factory.setCommandBus(commandBus());
        return factory;
    }
}
