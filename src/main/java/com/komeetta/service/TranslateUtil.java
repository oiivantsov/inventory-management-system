package com.komeetta.service;

import org.asynchttpclient.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class TranslateUtil {
    private static final String API_KEY = "164e85c7edmsh46fc2e7f121008fp1edfc2jsn7845f4b7c362";
    private static final String API_HOST = "google-translate113.p.rapidapi.com";
    private static final String ENDPOINT = "https://google-translate113.p.rapidapi.com/api/v1/translator/text";

    public static CompletableFuture<String> translate(String text, String targetLang) {
        AsyncHttpClient client = Dsl.asyncHttpClient();

        JSONObject body = new JSONObject();
        body.put("from", "auto");
        body.put("to", targetLang);
        body.put("text", text);

        return client.prepare("POST", ENDPOINT)
                .setHeader("x-rapidapi-key", API_KEY)
                .setHeader("x-rapidapi-host", API_HOST)
                .setHeader("Content-Type", "application/json")
                .setBody(body.toString())
                .execute()
                .toCompletableFuture()
                .thenApply(response -> {
                    try {
                        client.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    JSONObject json = new JSONObject(response.getResponseBody());
                    return json.getString("trans");
                });
    }
}
