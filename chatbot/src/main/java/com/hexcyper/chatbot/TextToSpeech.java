// TextToSpeech.java
package com.hexcyper.chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TextToSpeech {

    private final String apiKey;
    private final String apiEndpoint;

    public TextToSpeech(String apiKey, String apiEndpoint) {
        this.apiKey = apiKey;
        this.apiEndpoint = apiEndpoint;
    }

    public String synthesizeTextToSpeech(String text) {
        try {
            // Build the request URL
            String requestUrl = apiEndpoint +
                    "?voice_code=en-US-1" +
                    "&text=" + text +
                    "&speed=1.00" +
                    "&pitch=1.00" +
                    "&output_type=audio_url";

            // Create a URL object
            URL url = new URL(requestUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set request headers
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("X-RapidAPI-Key", apiKey);
            connection.setRequestProperty("X-RapidAPI-Host", "cloudlabs-text-to-speech.p.rapidapi.com");

            // Enable input/output streams
            connection.setDoOutput(true);

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Read the response from the API
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // Read the response line by line
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Close the connection
            connection.disconnect();

            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
