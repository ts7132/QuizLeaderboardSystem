package com.srm.quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LeaderboardProcessor {

    private final Set<String> processedEvents = new HashSet<>();
    private final Map<String, Integer> participantScores = new HashMap<>();

    /**
     * Processes a list of events. Deduplicates based on (roundId + participant).
     */
    public void processEvents(List<Models.Event> events) {
        if (events == null) {
            return;
        }

        for (Models.Event event : events) {
            String uniqueKey = event.roundId + "_" + event.participant;
            if (!processedEvents.contains(uniqueKey)) {
                processedEvents.add(uniqueKey);
                
                int currentScore = participantScores.getOrDefault(event.participant, 0);
                participantScores.put(event.participant, currentScore + event.score);
            } else {
                // Event is a duplicate, ignore
                System.out.println("Ignoring duplicate event: " + event.participant + " in " + event.roundId);
            }
        }
    }

    /**
     * Generates the final leaderboard sorted by totalScore in descending order.
     */
    public List<Models.LeaderboardEntry> generateLeaderboard() {
        List<Models.LeaderboardEntry> leaderboard = new ArrayList<>();
        
        for (Map.Entry<String, Integer> entry : participantScores.entrySet()) {
            leaderboard.add(new Models.LeaderboardEntry(entry.getKey(), entry.getValue()));
        }

        // Sort by totalScore descending
        leaderboard.sort((e1, e2) -> Integer.compare(e2.totalScore, e1.totalScore));

        return leaderboard;
    }
}
