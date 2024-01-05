// ChatController.java
package com.hexcyper.chatbot;

import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {

    // Directly specify the API key here
    private final String ttsApiKey = "684d469be0msh6bab57e58727967p1f3c33jsn338e9732dcc4";
    private final String ttsApiEndpoint = "https://cloudlabs-text-to-speech.p.rapidapi.com/synthesize";

    @PostMapping("/chat")
    @ResponseBody
    public String handlePrompt(@RequestParam String prompt, @RequestParam String model) {
        Main main = new Main();
        return main.run(prompt, model);
    }

    @PostMapping("/image")
    public String handleImagePrompt(@RequestBody String prompt, @RequestParam(required = false) String model) {
        HexCyperImgen imageGen = new HexCyperImgen();
        String response = imageGen.run(prompt, model);
        return response;
    }

    @PostMapping("/generateTextToSpeech")
    public String generateTextToSpeech(@RequestBody String text) {
        TextToSpeech textToSpeechGenerator = new TextToSpeech(ttsApiKey, ttsApiEndpoint);
        return textToSpeechGenerator.synthesizeTextToSpeech(text);
    }
}
