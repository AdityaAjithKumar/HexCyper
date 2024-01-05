package com.hexcyper.chatbot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {

    private static final String API_BASE = "https://api.naga.ac/v1";
    private static final String API_KEY = "DACpZfgc2QZDVLU1_Qj2amYLzSqVcmJgErCuaA5mdW8";

    public String run(String prompt, String model) {
        String endpoint = API_BASE + "/chat/completions";

        String postData = "{\n" +
                "    \"model\": \"" + model + "\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"You are a helpful assistant.\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"" + prompt + "\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Get the input stream from the connection
                InputStream istream = connection.getInputStream();

                // Create a buffer to store the partial response
                StringBuilder buffer = new StringBuilder();

                // Wrap the input stream in a BufferedReader for efficient reading
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(istream, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                }

                // Close the input stream
                istream.close();

                // Parse the complete JSON object
                JsonObject jsonObject = JsonParser.parseString(buffer.toString()).getAsJsonObject();

                // Extract the content
                JsonElement choicesElement = jsonObject.get("choices");

                if (choicesElement.isJsonArray()) {
                    JsonArray choicesArray = choicesElement.getAsJsonArray();
                    if (choicesArray.size() > 0) {
                        JsonElement firstChoice = choicesArray.get(0);
                        if (firstChoice.isJsonObject()) {
                            JsonElement messageElement = firstChoice.getAsJsonObject().get("message");
                            if (messageElement.isJsonObject()) {
                                return messageElement.getAsJsonObject().get("content").getAsString(); // Return the response content
                            }
                        }
                    }
                } else if (choicesElement.isJsonObject()) {
                    // Handle the case where "choices" is an object
                    JsonElement messageElement = choicesElement.getAsJsonObject().get("message");
                    if (messageElement.isJsonObject()) {
                        return messageElement.getAsJsonObject().get("content").getAsString(); // Return the response content
                    }
                }

            } else {
                return "Request failed with response code: " + responseCode;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }

        return "No valid response";
    }

    public static void main(String[] args) {
        // Example usage:
        Main chatbot = new Main();
        String prompt = "Tell me a joke";
        String model = "gpt-3.5-turbo";  // You can change the model here
        String response = chatbot.run(prompt, model);
        System.out.println("Response: " + response);
    }
}
