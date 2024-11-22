package com.gustavocarv.urlShotner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {
   private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> handleRequest(Map<String, Object> request, Context context) {
        String body = (String) request.get("body");
        if (body == null) {
            throw new IllegalArgumentException("Request body is missing");
        }

        Map<String, String> payload;
        try {
            payload = objectMapper.readValue(body, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse request body", e);
        }

        String originalUrl = payload.get("originalUrl");
        // expect format: "1d", "1h", "1M", "1w", "1y"
        String timeExpiration = payload.get("timeExpiration");

        if (originalUrl == null || timeExpiration == null) {
            throw new IllegalArgumentException("Missing required parameters: originalUrl or timeExpiration");
        }

        Map<String, String> response = new HashMap<>();
        response.put("shortUrl", UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

}