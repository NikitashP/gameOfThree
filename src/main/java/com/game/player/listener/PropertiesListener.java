package com.game.player.listener;

import static com.game.player.PlayerApplication.isLocalPortInUse;

import java.util.Properties;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

public class PropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        Properties props = new Properties();

        if (!isLocalPortInUse(8080)) {
            props.put("player.url", "http://localhost:8090/");
        } else {
            props.put("player.url", "http://localhost:8080/");
        }
        environment.getPropertySources().addFirst(new PropertiesPropertySource("custom", props));

    }

}