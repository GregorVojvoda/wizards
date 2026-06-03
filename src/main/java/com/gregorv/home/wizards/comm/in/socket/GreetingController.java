package com.gregorv.home.wizards.comm.in.socket;

import com.gregorv.home.wizards.vo.Greeting;
import com.gregorv.home.wizards.vo.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Hello, %s!".formatted(HtmlUtils.htmlEscape(message.getName())));
    }
}
