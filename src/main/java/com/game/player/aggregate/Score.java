package com.game.player.aggregate;

import java.io.Serializable;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.game.player.command.MakeMoveCommand;
import com.game.player.command.StartGameCommand;
import com.game.player.dto.MakeMoveCommandResponse;
import com.game.player.event.AddEvent;
import com.game.player.event.DivideEvent;
import com.game.player.event.InitializeScoreEvent;
import com.game.player.event.ResetScoreEvent;

@Aggregate
public class Score implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Score.class);

    private static final long serialVersionUID = 1L;

    @Value("${divisor:3}")
    private int DIVISOR = 3;

    @AggregateIdentifier
    private String id;

    private long value;

    public Score() {

    }

    @CommandHandler
    public Score(StartGameCommand startGameCommand, @Autowired AggregateTracker tracker) {
        final String id = startGameCommand.id;
        final long initialValue = startGameCommand.getInitialValue();
        AggregateLifecycle.apply(new InitializeScoreEvent(id, initialValue));
        tracker.track(id);
    }

    @EventSourcingHandler
    protected void on(InitializeScoreEvent event) {
        this.id = event.id;
        this.value = event.getInitialValue();
        LOGGER.info("Initialized aggregate {}, with value {}", id, event.getInitialValue());

    }

    @CommandHandler
    public MakeMoveCommandResponse on(MakeMoveCommand makeMoveCommand) {
        final String id = makeMoveCommand.id;
        final long updatedValue = makeMoveCommand.getValue();

        if (this.value != updatedValue) {
            AggregateLifecycle.apply(new ResetScoreEvent(id, updatedValue));
        }

        if (isDivisible(updatedValue)) {
            AggregateLifecycle.apply(new AddEvent(id, 0));
        } else if (isDivisible(updatedValue - 1)) {
            AggregateLifecycle.apply(new AddEvent(id, -1));
        } else if (isDivisible(updatedValue + 1)) {
            AggregateLifecycle.apply(new AddEvent(id, 1));
        }

        if (Math.abs(value) / DIVISOR >= 1) {
            AggregateLifecycle.apply(new DivideEvent(id, DIVISOR));
        }
        return new MakeMoveCommandResponse(this.id, value);
    }

    @EventSourcingHandler
    protected void on(AddEvent event) {
        this.id = event.id;
        LOGGER.info(
            "Added {}, to Current Value {}, Resulting value {}",
            event.getToAdd(),
            value,
            value + event.getToAdd());
        this.value += event.getToAdd();
    }

    @EventSourcingHandler
    protected void on(DivideEvent event) {
        this.id = event.id;
        LOGGER.info(
            "Divided by {}, to Current Value {}, Resulting value {}",
            event.getDivisor(),
            value,
            value / event.getDivisor());
        this.value = this.value / event.getDivisor();

    }

    private boolean isDivisible(double value) {
        return value % DIVISOR == 0;
    }

    @EventSourcingHandler
    protected void on(ResetScoreEvent event) {
        LOGGER.info(
            "Re-setting value of aggregate {}, to Current Value {},  from Value {}",
            this.id,
            event.getUpdatedValue(),
            this.value);
        this.id = event.id;
        this.value = event.getUpdatedValue();
    }
}
