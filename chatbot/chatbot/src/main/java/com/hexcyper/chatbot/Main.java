package com.hexcyper.chatbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    private static final String API_BASE = "https://api.naga.ac/v1";
    private static final String API_KEY = "DACpZfgc2QZDVLU1_Qj2amYLzSqVcmJgErCuaA5mdW8";
    private static final String MODEL = "gpt-3.5-turbo";

    public String run(String prompt) {
        String endpoint = API_BASE + "/chat/completions";

        String postData = "{\n" +
                "    \"model\": \"" + MODEL + "\",\n" +
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
                String response = readResponse(connection);

                // Parse the JSON response
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

                // Extract the assistant's message
                String assistantMessage = jsonResponse.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();

                return assistantMessage;
            } else {
                return "Request failed with response code: " + responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    private String readResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
