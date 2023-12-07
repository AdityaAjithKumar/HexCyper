package com.hexcyper.chatbot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class HexCyperImgen {

    public String run(String userPrompt) {
        try {
            // Your API endpoint
            String apiUrl = "https://api-inference.huggingface.co/models/Lykon/dreamshaper-7";

            // Set up the URL and open the connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set the request headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer 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");

            // Enable input/output streams
            connection.setDoOutput(true);

            // Build the JSON payload
            String jsonInputString = "{\"inputs\": \"" + userPrompt + "\"}";

            // Get the output stream and write the JSON payload to the connection
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the image bytes from the API response
            BufferedImage image = ImageIO.read(connection.getInputStream());

            // Convert the image to Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Return the Base64 string
            return base64Image;
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
}
