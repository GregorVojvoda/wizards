package com.gregorv.home.wizards.config;

import com.gregorv.home.wizards.comm.in.rest.GameController;
import com.gregorv.home.wizards.repo.GameRepo;
import com.gregorv.home.wizards.repo.GameRepoLocal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class AppConfig {

    @Bean
    public GameRepo gamesRepo() {
        return new GameRepoLocal();
    }

    @Bean
    public GameController gameController(GameRepo gameRepo, SimpMessagingTemplate messagingTemplate) {
        return new GameController(gameRepo, messagingTemplate);
    }
}
