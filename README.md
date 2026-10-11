# Native Android Tic-Tac-Toe Game

A modern, production-grade Android Tic-Tac-Toe application written in **Kotlin** and built with **Jetpack Compose** & **Material 3**.

---

## 🌟 Key Features

1. **Play vs Computer (Offline AI):**
   - **Easy:** Random move selection.
   - **Medium:** Heuristic checks for immediate wins and blocks.
   - **Impossible (Hard):** Complete **Minimax Algorithm with Alpha-Beta Pruning** guaranteeing optimal play (unbeatable).
2. **Online Multiplayer (PvP):**
   - **Room Code Matchmaking:** Host creates a 6-digit room code; guest joins via code.
   - **Turn Management & Timers:** 20-second per-turn countdown timer with automatic forfeit handling.
   - **Reconnection & Rematch:** Seamless in-game rematch requests and state synchronization.
   - **Dual Backend Architecture:**
     - Includes a zero-config reactive `SimulatedOnlineService` with artificial latency and player simulation for immediate testing.
     - Includes `FirebaseOnlineService` ready for Google Firebase Realtime Database deployment.
3. **Pass & Play (Local 2-Player):**
   - Play locally on the same device with alternating turns.
4. **Rich UI & Animations:**
   - Dark cyberpunk neon theme (`NeonBlue` & `NeonPink`).
   - Custom animated canvas rendering for `X` and `O`.
   - Dynamic winning-line highlight and animated scoreboard.
5. **CI/CD Automation:**
   - Pre-configured GitHub Actions workflow (`.github/workflows/build-apk.yml`) to automatically compile and publish the Android Game Installer (`.apk`) on every push or pull request.
   - Standard `.gitignore` tailored for Android Studio and Gradle.

---

## 🏗️ Architecture & Project Structure

The project adheres to Clean Architecture and MVVM (Model-View-ViewModel):

```
TicTacToeAndroid/
├── .github/
│   └── workflows/
│       └── build-apk.yml          # GitHub Actions CI for building the APK installer
├── .gitignore                     # Git ignore rules for Android Studio & Gradle
├── gradlew                        # Gradle Wrapper shell script (Unix/macOS/Linux)
├── gradlew.bat                    # Gradle Wrapper batch script (Windows)
├── app/
│   ├── build.gradle.kts
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/tictactoe/
│   │   │   ├── MainActivity.kt
│   │   │   ├── model/
│   │   │   │   ├── Player.kt
│   │   │   │   ├── GameMode.kt
│   │   │   │   ├── Difficulty.kt
│   │   │   │   ├── WinningLine.kt
│   │   │   │   └── GameState.kt
│   │   │   ├── game/
│   │   │   │   ├── TicTacToeEngine.kt
│   │   │   │   └── AiPlayer.kt
│   │   │   ├── online/
│   │   │   │   ├── OnlineGameService.kt
│   │   │   │   ├── SimulatedOnlineService.kt
│   │   │   │   └── FirebaseOnlineService.kt
│   │   │   ├── viewmodel/
│   │   │   │   └── TicTacToeViewModel.kt
│   │   │   └── ui/
│   │   │       ├── components/
│   │   │       │   ├── TicTacToeBoard.kt
│   │   │       │   ├── ScoreBoard.kt
│   │   │       │   └── TurnIndicator.kt
│   │   │       ├── screens/
│   │   │       │   ├── MainMenuScreen.kt
│   │   │       │   ├── GameScreen.kt
│   │   │       │   ├── OnlineLobbyDialog.kt
│   │   │       │   └── GameOverDialog.kt
│   │   │       └── theme/
│   │   │           ├── Color.kt
│   │   │           ├── Type.kt
│   │   │           └── Theme.kt
│   │   └── res/
│   │       └── values/
│   │           ├── strings.xml
│   │           ├── colors.xml
│   │           └── themes.xml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## 🚀 How to Build and Run

### Option 1: In Android Studio
1. Unzip `tictactoe_android_project.zip`.
2. Open **Android Studio** -> **File** -> **Open...** -> select the extracted `TicTacToeAndroid` folder.
3. Wait for Gradle sync to complete.
4. Select an emulator or connected physical Android device and click **Run** (`Shift + F10`).

### Option 2: Command Line Build
```bash
# Build the debug APK installer
./gradlew assembleDebug

# Output APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## ⚙️ Automated CI/CD (GitHub Actions)

When pushed to a GitHub repository:
1. The `.github/workflows/build-apk.yml` workflow triggers on push to `main`/`master` or via manual dispatch.
2. It sets up Ubuntu with JDK 17.
3. Executes `./gradlew assembleDebug`.
4. Uploads the generated APK file as a downloadable GitHub Action artifact (`TicTacToe-Installer-APK`).
