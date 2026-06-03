package com.gregorv.home.wizards.comm.in.rest;

import com.gregorv.home.wizards.vo.Greeting;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequestMapping("/api/greet")
public class GreetingRestController {

    private final SimpMessagingTemplate messagingTemplate;

    public GreetingRestController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public Greeting greet(@RequestParam String name) {
        String greetingMessage = "Hello, %s!".formatted(HtmlUtils.htmlEscape(name));
        Greeting greeting = new Greeting(greetingMessage);
        messagingTemplate.convertAndSend("/topic/greetings", greeting);
        return greeting;
    }
}
