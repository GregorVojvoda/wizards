package com.gregorv.home.wizards.comm.in.socket;

import com.gregorv.home.wizards.vo.GameSocketMessage;
import com.gregorv.home.wizards.vo.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GamesSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/games")
    public GameSocketMessage greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new GameSocketMessage("Hello, %s!".formatted(HtmlUtils.htmlEscape(message.getName())));
    }
}
