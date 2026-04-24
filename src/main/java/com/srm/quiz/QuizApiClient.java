package com.srm.quiz;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class QuizApiClient {

    private static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    private final HttpClient httpClient;
    private final Gson gson;

    public QuizApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public Models.QuizMessageResponse fetchPoll(String regNo, int pollIndex) {
        try {
            String url = BASE_URL + "/quiz/messages?regNo=" + regNo + "&poll=" + pollIndex;
            System.out.println("GET " + url);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), Models.QuizMessageResponse.class);
            } else {
                System.err.println("Error fetching poll " + pollIndex + ". Status Code: " + response.statusCode());
                System.err.println("Response: " + response.body());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Exception fetching poll " + pollIndex + ": " + e.getMessage());
            return null;
        }
    }

    public Models.SubmitResponse submitLeaderboard(Models.SubmitRequest submitRequest) {
        try {
            String url = BASE_URL + "/quiz/submit";
            String jsonPayload = gson.toJson(submitRequest);
            System.out.println("POST " + url);
            System.out.println("Payload: " + jsonPayload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return gson.fromJson(response.body(), Models.SubmitResponse.class);
            } else {
                System.err.println("Error submitting leaderboard. Status Code: " + response.statusCode());
                System.err.println("Response: " + response.body());
                // Still try to parse to see if it returned SubmitResponse structure with error messages
                try {
                    return gson.fromJson(response.body(), Models.SubmitResponse.class);
                } catch (Exception ignored) {
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("Exception submitting leaderboard: " + e.getMessage());
            return null;
        }
    }
}
