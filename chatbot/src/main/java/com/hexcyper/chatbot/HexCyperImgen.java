package com.hexcyper.chatbot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HexCyperImgen {

    private static final String API_KEY = "DACpZfgc2QZDVLU1_Qj2amYLzSqVcmJgErCuaA5mdW8";

    public String run(String prompt, String model) {
        try {
            // Your API endpoint
            String apiUrl = "https://api.naga.ac/v1/images/generations";

            // Create JSON payload
            String jsonPayload = String.format("{\"model\":\"%s\", \"prompt\":\"%s\", \"size\":\"1024x1024\", \"n\":1, \"response_format\":\"url\"}", model, prompt);

            // Build the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "Error: " + response.statusCode() + ", " + response.body();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
}
