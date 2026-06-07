package com.gregorv.home.wizards.comm.in.rest;

import com.gregorv.home.wizards.repo.GameRepo;
import com.gregorv.home.wizards.vo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameRepo repository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String GAMES_TOPIC_PATH = "/topic/games";

    public GameController(GameRepo repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping(path = "/init")
    public ResponseEntity<Void> gameInit(@RequestBody InitializeGameRequest request) {
        repository.initGame(request.gameName(), request.aliases());
        return ResponseEntity.ok().body(null);
    }

    @GetMapping(path = "/{gameName}/score")
    public ResponseEntity<Map<String, BigInteger>> getScore(@PathVariable("gameName") String gameName) {
        WizardsGame game = repository.getGame(gameName);
        return ResponseEntity.ok(game.getScore());
    }

    @GetMapping(path = "/{gameName}/score-board")
    public ResponseEntity<Map<Integer, Map<String, Round>>> getScoreBoard(@PathVariable("gameName") String gameName) {
        WizardsGame game = repository.getGame(gameName);
        return ResponseEntity.ok(game.getScoreboard());
    }

    @PostMapping(path = "/{gameName}/round/predict")
    public ResponseEntity<Void> setRoundPrediction(@PathVariable("gameName") String gameName, @RequestBody Map<String, Integer> prediction) {
        repository.setGameRoundPrediction(gameName, prediction);
        WizardsGame game = repository.getGame(gameName);

        sendMessageToGamesSocket(GameSocketMessage.tableUpdate(game.getScoreboard()));
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(path = "/{gameName}/round/result")
    public ResponseEntity<Void> setRoundResult(@PathVariable("gameName") String gameName, @RequestBody Map<String, Integer> result) {
        repository.setGameRoundResult(gameName, result);
        WizardsGame game = repository.getGame(gameName);
        sendMessageToGamesSocket(GameSocketMessage.tableUpdate(game.getScoreboard()));

        if (game.getRound() > game.gameNumberOfRounds()) {
            sendMessageToGamesSocket(GameSocketMessage.gameFinish(game.getScore()));
        }

        return ResponseEntity.ok().body(null);
    }

    private void sendMessageToGamesSocket(GameSocketMessage gameSocketMessage) {
        messagingTemplate.convertAndSend(GAMES_TOPIC_PATH, gameSocketMessage);
    }

}
