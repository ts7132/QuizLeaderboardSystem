# Quiz Leaderboard System 🏆

This repository contains the complete Java-based backend integration solution for the **Quiz Leaderboard System** assignment. 

The application is designed to simulate a real-world distributed system problem where API responses from an external validator might deliver duplicate data across multiple polls. The system correctly fetches, deduplicates, aggregates, and submits the final leaderboard results.

---

## 🚀 Features

- **Robust API Polling:** Automatically polls the validator API (`GET /quiz/messages`) 10 times to collect all quiz events.
- **Strict Rate Limiting:** Enforces a mandatory 5-second delay between consecutive API requests.
- **Smart Deduplication:** Identifies and filters out duplicate events using a unique composite key (`roundId` + `participant`).
- **Score Aggregation:** Correctly computes the total score for each participant across all valid quiz rounds.
- **Leaderboard Generation:** Generates the final leaderboard sorted in descending order by `totalScore`.
- **Automated Submission:** Posts the final processed leaderboard to the validator submission API (`POST /quiz/submit`).
- **Zero External HTTP Dependencies:** Built entirely with standard Java 11 `HttpClient` for maximum portability and minimal bloat.

---

## 🏗 Architecture & Design

The codebase is organized into a clean, object-oriented structure within the `com.srm.quiz` package:

1. **`Main.java`**: The core orchestrator. It manages the application lifecycle, executing the 10 polls, enforcing the 5-second sleep delays, calling the processor, and submitting the results.
2. **`QuizApiClient.java`**: Dedicated HTTP client layer. Handles all REST network communication (GET for fetching and POST for submissions).
3. **`LeaderboardProcessor.java`**: Contains the core business logic.
   - Utilizes a `HashSet` to keep track of processed event identifiers (`roundId_participant`).
   - Utilizes a `HashMap` to dynamically accumulate total scores per participant.
   - Handles the descending sort logic for the final leaderboard.
4. **`Models.java`**: Data Transfer Objects (DTOs) that map strictly to the JSON schema specified in the assignment requirements.

---

## 🛠 Prerequisites

To compile and run this project, ensure you have the following installed on your system:

- **Java 11** or higher (OpenJDK / Oracle JDK)
- **Apache Maven 3.6+**

---

## 💻 How to Build and Run

### 1. Clone the Repository
```bash
git clone https://github.com/ts7132/QuizLeaderboardSystem.git
cd QuizLeaderboardSystem
```

### 2. Compile the Project
Use Maven to clean the target directory, resolve dependencies (`gson`), and compile the source code:
```bash
mvn clean compile
```

### 3. Run the Application
By default, the application runs using the example registration number `2024CS101`. To execute it, run:
```bash
mvn exec:java
```

If you need to test the application using your **actual Registration Number**, you can pass it dynamically as a command-line argument:
```bash
mvn exec:java -Dexec.args="YOUR_REG_NO_HERE"
```

---

## 🔍 Expected Execution Output

When you run the application, it will log its real-time progress to the console. You should see 10 polls with 5-second waiting periods in between, followed by the deduplicated leaderboard, and finally the submission verification response.

```text
Starting Quiz Leaderboard System for regNo: 2024CS101
--- Polling Index: 0 ---
GET https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/messages?regNo=2024CS101&poll=0
Waiting 5 seconds before next poll...
--- Polling Index: 1 ---
GET https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/messages?regNo=2024CS101&poll=1
Waiting 5 seconds before next poll...

... (polls 2 through 8) ...
Ignoring duplicate event: Alice in R1
Ignoring duplicate event: Charlie in R2

--- Polling Index: 9 ---
GET https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/messages?regNo=2024CS101&poll=9

--- Final Leaderboard ---
Bob: 295
Alice: 280
Charlie: 260

--- Submitting Leaderboard ---
POST https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/submit
Payload: {"regNo":"2024CS101","leaderboard":[{"participant":"Bob","totalScore":295},{"participant":"Alice","totalScore":280},{"participant":"Charlie","totalScore":260}]}

--- Submission Result ---
Is Correct: true
Is Idempotent: true
Submitted Total: 835
Expected Total: 835
Message: Correct!
```

---

## 🧩 Dependencies
- [Google Gson (2.10.1)](https://github.com/google/gson) - Used strictly for JSON serialization and deserialization.
