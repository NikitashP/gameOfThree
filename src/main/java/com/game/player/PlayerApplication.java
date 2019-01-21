package com.game.player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.game.player.controller.GameController;
import com.game.player.listener.PropertiesListener;

@SpringBootApplication
public class PlayerApplication implements ApplicationRunner {

    @Autowired
    private GameController gameController;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerApplication.class);

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(PlayerApplication.class);
        if (!isLocalPortInUse(8080)) {

            Properties properties = new Properties();
            properties.put("server.port", "8080");
            properties.put("spring.application.name", "player1");
            app.setDefaultProperties(properties);

            LOGGER.info("Instance started as Player 1");

        } else if (!isLocalPortInUse(8090)) {

            Properties properties = new Properties();
            properties.put("server.port", "8090");
            properties.put("spring.application.name", "player2");
            app.setDefaultProperties(properties);

            LOGGER.info("Instance started as Player 2");

        } else {
            throw new IllegalStateException("Only 2 Players can play");
        }
        LOGGER.info("Arguments {}", args);
        app.addListeners(new PropertiesListener());
        app.run(args);

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

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (args.getNonOptionArgs().contains("auto")) {
            LOGGER.info("game started in Auto Mode");
            final GameController.StartValue startValue = new GameController.StartValue();
            Random random = new Random();
            startValue.initialValue = random.nextLong();
            gameController.startGame(startValue);
        }

    }
}
