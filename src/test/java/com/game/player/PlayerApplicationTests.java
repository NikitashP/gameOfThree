package com.game.player;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.game.player.aggregate.AggregateTracker;
import com.game.player.aggregate.Score;
import com.game.player.command.MakeMoveCommand;
import com.game.player.command.StartGameCommand;
import com.game.player.event.AddEvent;
import com.game.player.event.DivideEvent;
import com.game.player.event.InitializeScoreEvent;
import com.game.player.event.ResetScoreEvent;

public class PlayerApplicationTests {

    private FixtureConfiguration fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture(Score.class);
        fixture.registerInjectableResource(Mockito.mock(AggregateTracker.class));
    }

    @Test
    public void startGame() {
        fixture.given().when(new StartGameCommand("id", 123L)).expectSuccessfulHandlerExecution().expectEvents(
            new InitializeScoreEvent("id", 123L));
    }

    @Test
    public void makeMoveInTheGameWhenInitialScoreIsDivisibleByThreeAfterAddingZero() {
        fixture.given(new InitializeScoreEvent("id", 12))
                .when(new MakeMoveCommand("id", 12))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new AddEvent("id", 0), new DivideEvent("id", 3));
    }

    @Test
    public void makeMoveInTheGameWhenInitialScoreIsNotDivisibleByThree() {
        fixture.given(new InitializeScoreEvent("id", 13))
                .when(new MakeMoveCommand("id", 12))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ResetScoreEvent("id", 12), new AddEvent("id", 0), new DivideEvent("id", 3));
    }

    @Test
    public void makeMoveInTheGameWhenInitialScoreIsDivisibleByThreeAfterAddingOne() {
        fixture.given(new InitializeScoreEvent("id", 11))
                .when(new MakeMoveCommand("id", 11))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new AddEvent("id", 1), new DivideEvent("id", 3));
    }

    @Test
    public void makeMoveInTheGameWhenInitialScoreIsDivisibleByThreeAfterSubstractingOne() {
        fixture.given(new InitializeScoreEvent("id", -8))
                .when(new MakeMoveCommand("id", -8))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new AddEvent("id", -1), new DivideEvent("id", 3));
    }

    @Test
    public void makeMoveOnInexistentScore() {
        fixture.given().when(new MakeMoveCommand("id", 12L)).expectException(AggregateNotFoundException.class);
    }

}
