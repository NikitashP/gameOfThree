# gameOfThree

#### Logic

1. 2 instances of Player applications need to be run
2. Player 1 will latch on port 8080 while player 2 will start on port 8090
3. all changes to the score are stored as events in the event store

### Frameworks used
1. Axon
2. Spring

### Starting the application

### With Auto Mode
start one instance with auto argument as shown below followed by another instance of application
#### Player 1 on auto mode
java -jar player-0.0.1-SNAPSHOT.jar auto
#### Player 2
java -jar player-0.0.1-SNAPSHOT.jar

### Winner will be reported on the console
```text
21-01-2019 15:40:20.687 [pool-1-thread-2] [player1] INFO  com.game.player.controller.GameController.onSuccess - GAME OVER !!
```

### With Manual Mode
start both the instances and then fire 
```text
POST http://localhost:8080/game/start
{
	"initialValue":100
}
```

#### Player 1
java -jar player-0.0.1-SNAPSHOT.jar
#### Player 2
java -jar player-0.0.1-SNAPSHOT.jar
### Winner will be reported on the console
```text
21-01-2019 15:40:20.687 [pool-1-thread-2] [player1] INFO  com.game.player.controller.GameController.onSuccess - GAME OVER !!
```

## API
### to make a move in the game
```text
curl -X PUT --data 100 -H "Content-Type: application/json" localhost:8090/game/{scoreId}/move
```
### events as list of all individual moves made against a particular aggregate
```text
curl -X GET localhost:8090/game/{scoreId}/events/
```

### Open Concerns
1. minor gap between the application start and port capture can lead to one instance not starting.
2. events API shows all changes made against the aggregate inside its instance. Consolidated events from both the instances when sorted by Time can give exact details of all delta against the aggregate.
3. Code coverage is not 100%