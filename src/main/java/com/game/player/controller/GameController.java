package com.game.player.controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.game.player.aggregate.AggregateTracker;
import com.game.player.command.MakeMoveCommand;
import com.game.player.command.StartGameCommand;
import com.game.player.dto.MakeMoveCommandResponse;
import com.game.player.exception.InvalidInput;

@RequestMapping("/game")
@RestController
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final CommandGateway commandGateway;

    private final EventStore eventStore;

    @Autowired
    private AggregateTracker aggregateTracker;

    @Value("${player.url}")
    private String playerUrl;

    public GameController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping("/start")
    public void startGame(@RequestBody StartValue startValue) throws ExecutionException, InterruptedException {
        if (startValue.initialValue == 0) {
            throw new InvalidInput("Input should not be Zero");
        }
        String id = UUID.randomUUID().toString();
        final CompletableFuture<String> aggregateId = commandGateway
                .send(new StartGameCommand(id, startValue.initialValue));
        invokeMoveOfOtherPlayer(new MakeMoveCommandResponse(aggregateId.get(), startValue.initialValue));

    }

    @PutMapping(path = "{scoreId}/move")
    public void move(@RequestBody long value, @PathVariable String scoreId)
            throws ExecutionException,
            InterruptedException {

        if (!aggregateHasAlreadyBeenMirrored(scoreId)) {
            commandGateway.send(new StartGameCommand(scoreId, value)).get();
        }

        commandGateway.send(
            new MakeMoveCommand(scoreId, value),
            new CommandCallback<MakeMoveCommand, MakeMoveCommandResponse>() {

                @Override
                public void onSuccess(
                        CommandMessage<? extends MakeMoveCommand> commandMessage,
                        MakeMoveCommandResponse response) {
                    if (Math.abs(response.getValue()) == 1) {
                        LOGGER.info("GAME OVER !!");
                    } else {
                        try {
                            invokeMoveOfOtherPlayer(response);
                        } catch (InterruptedException e) {
                            LOGGER.error("Unable to Make Move with another player", e);
                        }
                    }

                }

                @Override
                public void onFailure(CommandMessage<? extends MakeMoveCommand> commandMessage, Throwable throwable) {
                    LOGGER.info("error", throwable.getCause());
                }
            });

        LOGGER.info("Move completed");
    }

    private boolean aggregateHasAlreadyBeenMirrored(String scoreId) {
        return aggregateTracker.contains(scoreId);
    }

    private void invokeMoveOfOtherPlayer(MakeMoveCommandResponse response) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            LOGGER.info("Initiate Move");
            restTemplate.put(playerUrl + "game/" + response.getId() + "/move", response.getValue());
            LOGGER.info("Move Complete");
        } catch (RuntimeException exception) {
            LOGGER.error("Player not yet available", exception);
            Thread.sleep(5000);
            invokeMoveOfOtherPlayer(response);
        }

    }

    @GetMapping("{id}/events")
    public List<Object> getEvents(@PathVariable String id) {
        return eventStore.readEvents(id).asStream().map(s -> s.getPayload()).collect(Collectors.toList());
    }

    public static class StartValue {

        public long initialValue = 0;
    }

    @ExceptionHandler(AggregateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
    }

    @ExceptionHandler(InvalidInput.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidInput(InvalidInput exception) {
        return exception.getMessage();
    }
}
