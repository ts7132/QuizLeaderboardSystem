package com.srm.quiz;

import java.util.List;

public class Models {

    public static class Event {
        public String roundId;
        public String participant;
        public int score;
    }

    public static class QuizMessageResponse {
        public String regNo;
        public String setId;
        public int pollIndex;
        public List<Event> events;
    }

    public static class LeaderboardEntry {
        public String participant;
        public int totalScore;

        public LeaderboardEntry(String participant, int totalScore) {
            this.participant = participant;
            this.totalScore = totalScore;
        }
    }

    public static class SubmitRequest {
        public String regNo;
        public List<LeaderboardEntry> leaderboard;

        public SubmitRequest(String regNo, List<LeaderboardEntry> leaderboard) {
            this.regNo = regNo;
            this.leaderboard = leaderboard;
        }
    }

    public static class SubmitResponse {
        public boolean isCorrect;
        public boolean isIdempotent;
        public int submittedTotal;
        public int expectedTotal;
        public String message;
    }
}
