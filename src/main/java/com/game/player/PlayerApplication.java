package com.game.player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.game.player.listener.PropertiesListener;

@SpringBootApplication
public class PlayerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerApplication.class);

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(PlayerApplication.class);
        List<String> arguments = Arrays.stream(args).collect(Collectors.toList());
        if (!isLocalPortInUse(8080)) {

            app.setDefaultProperties(Collections.singletonMap("server.port", "8080"));

            MDC.put("playerId", "player1");
            LOGGER.info("Instance started as Player 1");

        } else if (!isLocalPortInUse(8090)) {

            app.setDefaultProperties(Collections.singletonMap("server.port", "8090"));

            MDC.put("playerId", "player2");
            LOGGER.info("Instance started as Player 2");

        } else {
            throw new IllegalStateException("Only 2 Players can play");
        }
        LOGGER.info("Arguments {}", args);

        app.addListeners(new PropertiesListener());
        app.run(arguments.toArray(new String[arguments.size()]));

    }

    public static boolean isLocalPortInUse(int port) {
        try {
            // ServerSocket try to open a LOCAL port
            new ServerSocket(port).close();
            // local port can be opened, it's available
            return false;
        } catch (IOException e) {
            // local port cannot be opened, it's in use
            return true;
        }
    }

}
