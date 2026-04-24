package com.srm.quiz;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String regNo = "2024CS101"; // Default registration number

        if (args.length > 0) {
            regNo = args[0];
        }

        System.out.println("Starting Quiz Leaderboard System for regNo: " + regNo);

        QuizApiClient apiClient = new QuizApiClient();
        LeaderboardProcessor processor = new LeaderboardProcessor();

        // 1. Poll the validator API 10 times
        for (int i = 0; i < 10; i++) {
            System.out.println("--- Polling Index: " + i + " ---");
            Models.QuizMessageResponse response = apiClient.fetchPoll(regNo, i);

            if (response != null) {
                // 2. & 3. Deduplicate and Aggregate
                processor.processEvents(response.events);
            }

            // 5 second mandatory delay between polls
            if (i < 9) { // No need to wait after the last poll
                try {
                    System.out.println("Waiting 5 seconds before next poll...");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Polling interrupted: " + e.getMessage());
                    break;
                }
            }
        }

        // 4. Generate Leaderboard
        List<Models.LeaderboardEntry> finalLeaderboard = processor.generateLeaderboard();

        System.out.println("\n--- Final Leaderboard ---");
        for (Models.LeaderboardEntry entry : finalLeaderboard) {
            System.out.println(entry.participant + ": " + entry.totalScore);
        }

        // 5. Submit Leaderboard
        System.out.println("\n--- Submitting Leaderboard ---");
        Models.SubmitRequest request = new Models.SubmitRequest(regNo, finalLeaderboard);
        Models.SubmitResponse submitResponse = apiClient.submitLeaderboard(request);

        if (submitResponse != null) {
            System.out.println("\n--- Submission Result ---");
            System.out.println("Is Correct: " + submitResponse.isCorrect);
            System.out.println("Is Idempotent: " + submitResponse.isIdempotent);
            System.out.println("Submitted Total: " + submitResponse.submittedTotal);
            System.out.println("Expected Total: " + submitResponse.expectedTotal);
            System.out.println("Message: " + submitResponse.message);
        } else {
            System.err.println("Submission failed or no response received.");
        }
    }
}
