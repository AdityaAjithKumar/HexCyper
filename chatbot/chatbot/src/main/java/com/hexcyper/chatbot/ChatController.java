package com.hexcyper.chatbot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @PostMapping("/chat")
    public String handlePrompt(@RequestBody String prompt) {
        Main main = new Main();
        String response = main.run(prompt);
        return response;
    }

    @PostMapping("/image")
    public String handleImagePrompt(@RequestBody String prompt) {
        HexCyperImgen imageGen = new HexCyperImgen();
        String response = imageGen.run(prompt);
        return response;
    }
}
