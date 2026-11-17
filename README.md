# Campus Copilot

An Android app for university students: ask questions about your campus, plan study sessions,
get a concept explained and test yourself with a quick quiz. Built with Kotlin and Jetpack Compose.

This is an early prototype. The screens and the app's structure are in place, but the AI and the
campus data are local stand-ins (see [Status](#status)).

## What it does

The app has five tabs.

| Tab | What you can do |
|---|---|
| Home | A search box for the assistant, quick actions (study plan, explain, quiz me, ask campus) and "continue where you left off" |
| Ask | Chat with the assistant. Campus questions are answered only from the campus knowledge base, with the sources listed under the answer. If nothing matches, it says so instead of guessing |
| Study | **Plan**: enter a subject, topic, available time, deadline and difficulty, and get a time-blocked study plan you can tick off. **Explain**: a plain explanation of a concept with an example, common mistakes and one check question. **Quiz**: a short multiple-choice quiz on a topic, with an explanation for each answer |
| Saved | Plans, answers and quizzes you saved, filtered by type |
| Profile | Your stats, plus settings to switch dark mode and clear saved content or chat history |

The campus knowledge base covers facilities, clubs, sports, academics, events, the library,
the cafeteria, hostels and transport.

## How it is built

- Kotlin, Jetpack Compose and Material 3, minimum Android 7.0 (API 24), target API 36.
- The app talks to two interfaces in `domain/services`:
  - `AIService` makes study plans, explanations and quizzes, and answers chat and campus questions.
  - `CampusRetriever` finds the campus documents that match a question.
- Repositories in `domain/repositories` hold study plans, quiz results and saved items.
- `data/` has the current implementations of all of these.

The campus answers follow a retrieve-then-answer pattern: `CampusRetriever` returns the most
relevant documents for the question, and `AIService.answerCampusQuestion` writes the answer from
those documents only. That keeps answers tied to real campus information and makes the sources
easy to show.

```
app/src/main/java/com/example/campuscopilot/
  MainActivity.kt
  ui/navigation/   bottom navigation and the five tabs
  ui/screens/      home, ask, study, saved, profile
  ui/components/   shared UI pieces
  domain/          interfaces for the AI service, retriever and repositories
  data/models/     study plans, quizzes, chat messages, campus documents, saved items
  data/services/   MockAIService, MockCampusRetriever
  data/repositories/  in-memory repositories
  theme/           colours, type and theme
```

## Status

- **AI:** `MockAIService` returns fixed, template answers after a short delay. No model is called yet.
- **Campus data:** `MockCampusRetriever` holds 22 sample campus documents and ranks them by
  keyword matches against the title, text and tags. The documents describe a sample campus, not a real one.
- **Storage:** plans, quiz results and saved items live in memory and reset when the app closes.

Possible next steps: connect `AIService` to a real model, load the knowledge base from your
college's own information, rank documents with embeddings, and save data on the device.

## Run it

Requires Android Studio with JDK 21.

1. Open the project folder in Android Studio and let Gradle sync.
2. Pick an emulator or a connected phone and press Run.

From the command line:

```bash
./gradlew installDebug
```
