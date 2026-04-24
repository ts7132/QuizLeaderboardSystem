# Quiz Leaderboard System

This project is a Java-based solution for the "Quiz Leaderboard System" assignment. It simulates a real-world backend integration problem by consuming API responses from an external validator, processing them with deduplication, and producing a final sorted leaderboard.

## Features

- **API Polling**: Polls the validator API 10 times to collect all events.
- **Rate Limiting**: Implements a mandatory 5-second delay between poll requests.
- **Deduplication**: Filters out duplicate API response data by uniquely identifying events using `(roundId + participant)`.
- **Aggregation**: Accurately computes the total score across all rounds per participant.
- **Leaderboard Generation**: Generates and submits a sorted leaderboard (by `totalScore` descending) to the submission API.

## Architecture & Design

The application consists of the following core components:

- `Main.java`: The entry point that orchestrates the workflow. It manages the loop for polling, enforces the sleep delay, calls the processor, and submits the final result.
- `QuizApiClient.java`: Handles all network communication (HTTP GET for polling and HTTP POST for submission). Built using standard Java 11 `HttpClient`.
- `LeaderboardProcessor.java`: Contains the core business logic.
  - Maintains a `HashSet` of processed event identifiers (`roundId_participant`).
  - Maintains a `HashMap` of participant total scores.
  - Ensures deduplication and aggregates correctly.
- `Models.java`: Contains simple Java objects representing the API schema for Gson JSON mapping.

## Prerequisites

- **Java 11 or higher**
- **Maven 3.6+**

## How to Build & Run

### Compiling the code
Run the following command from the project root:

```bash
mvn clean install
```

### Running the application
By default, the application uses the registration number `2024CS101`. You can run it via Maven:

```bash
mvn exec:java
```

If you wish to provide a specific registration number, pass it as an argument:

```bash
mvn exec:java -Dexec.args="YOUR_REG_NO"
```

## Expected Output

You should see logs for each poll request, followed by waiting logs. After 10 polls, the application will display the final leaderboard and the result of the submission.

```
Starting Quiz Leaderboard System for regNo: 2024CS101
--- Polling Index: 0 ---
GET https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/messages?regNo=2024CS101&poll=0
Waiting 5 seconds before next poll...
...
...
--- Submitting Leaderboard ---
POST https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/submit
Payload: {"regNo":"2024CS101","leaderboard":[...]}

--- Submission Result ---
Is Correct: true
Is Idempotent: true
Submitted Total: ...
Expected Total: ...
Message: Correct!
```
