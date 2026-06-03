package com.gregorv.home.wizards.comm.in.rest;

import com.gregorv.home.wizards.vo.InitializeGameRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/init")
public class InitializeGameController {

    @PostMapping
    public ResponseEntity<String> gameInit(@RequestBody InitializeGameRequest request) {

        return ResponseEntity.ok(request.toString());
    }

}
