package com.example.campuscopilot.data.services

import com.example.campuscopilot.data.models.*
import com.example.campuscopilot.domain.services.AIService
import kotlinx.coroutines.delay

class MockAIService : AIService {

    override suspend fun generateStudyPlan(
        subject: String,
        topic: String,
        availableTime: String,
        deadline: String,
        difficulty: String
    ): StudyPlan {
        delay(800) // Realistic AI generation delay

        val title = "$subject — $topic Study Roadmap"
        val blocks = listOf(
            StudyBlock(timeRange = "0:00 – 0:20", activity = "Core Concept & Syntax Review ($topic)"),
            StudyBlock(timeRange = "0:20 – 1:00", activity = "Guided Hands-on Code & Practice Problems"),
            StudyBlock(timeRange = "1:00 – 1:10", activity = "Rest & Hydration Break"),
            StudyBlock(timeRange = "1:10 – 1:50", activity = "Challenge Exercises & Edge Case Traps"),
            StudyBlock(timeRange = "1:50 – 2:00", activity = "Final Self-Quiz & Mindmap Summary")
        )

        return StudyPlan(
            title = title,
            subject = subject,
            topic = topic,
            availableTime = availableTime,
            deadline = deadline,
            difficulty = difficulty,
            priority = "High Priority",
            blocks = blocks
        )
    }

    override suspend fun explainConcept(
        concept: String,
        difficulty: String
    ): ConceptExplanation {
        delay(700)

        val cleanConcept = concept.trim()
        val isPythonLoop = cleanConcept.lowercase().contains("loop") || cleanConcept.lowercase().contains("python")
        val isRecursion = cleanConcept.lowercase().contains("recursion")

        return when {
            isRecursion -> ConceptExplanation(
                concept = concept,
                difficulty = difficulty,
                simpleExplanation = "Recursion is when a function calls itself to solve a smaller piece of the same problem, until it hits a simple base case.",
                example = "fun factorial(n: Int): Int = if (n <= 1) 1 else n * factorial(n - 1)",
                commonMistakes = "1. Forgetting the Base Case (causes StackOverflowError).\n2. Not moving toward the base case in the recursive call.",
                quickTestQuestion = "What happens if a recursive function never reaches its base case?"
            )
            isPythonLoop -> ConceptExplanation(
                concept = concept,
                difficulty = difficulty,
                simpleExplanation = "Loops repeat a block of code multiple times. A 'for' loop iterates over a collection, while a 'while' loop runs as long as a condition is true.",
                example = "for item in ['apple', 'banana', 'cherry']:\n    print(item)",
                commonMistakes = "1. Off-by-one errors when using range(n).\n2. Modifying a list while iterating over it.",
                quickTestQuestion = "What values does range(1, 5) generate in Python?"
            )
            else -> ConceptExplanation(
                concept = concept,
                difficulty = difficulty,
                simpleExplanation = "$concept is a foundational topic in $difficulty studies. It structures how data or logic is processed step-by-step.",
                example = "// Example demonstration of $concept\nval input = list.map { process(it) }",
                commonMistakes = "1. Overcomplicating basic edge cases.\n2. Omitting error boundaries.",
                quickTestQuestion = "What is the primary benefit of using $concept?"
            )
        }
    }

    override suspend fun generateQuiz(
        topic: String,
        numQuestions: Int,
        difficulty: String
    ): Quiz {
        delay(900)

        val sampleQuestions = listOf(
            QuizQuestion(
                question = "What does the `range(3)` function produce in a Python for-loop?",
                options = listOf("0, 1, 2", "1, 2, 3", "0, 1, 2, 3", "1, 2"),
                correctOptionIndex = 0,
                explanation = "`range(3)` produces integers starting from 0 up to (but not including) 3."
            ),
            QuizQuestion(
                question = "Which keyword is used to skip the rest of the current iteration in a loop?",
                options = listOf("break", "continue", "pass", "exit"),
                correctOptionIndex = 1,
                explanation = "`continue` immediately skips the remainder of the current loop iteration and moves to the next."
            ),
            QuizQuestion(
                question = "What happens if a `while` loop condition never becomes False?",
                options = listOf("Syntax Error", "Infinite Loop", "Returns null", "Automatic exit after 100 iterations"),
                correctOptionIndex = 1,
                explanation = "If the condition remains True continuously, the program gets stuck in an infinite loop."
            ),
            QuizQuestion(
                question = "How do you check if an element exists inside a list in Python?",
                options = listOf("element.exists(list)", "list.has(element)", "element in list", "contains(list, element)"),
                correctOptionIndex = 2,
                explanation = "The `in` operator evaluates to True if the item is present in the sequence."
            )
        )

        return Quiz(
            topic = topic,
            difficulty = difficulty,
            questions = sampleQuestions.take(numQuestions.coerceAtLeast(1))
        )
    }

    override suspend fun chat(
        prompt: String,
        conversationHistory: List<ChatMessage>
    ): ChatMessage {
        delay(600)

        val cleanPrompt = prompt.lowercase()
        val responseText = when {
            cleanPrompt.contains("study plan") || cleanPrompt.contains("python") ->
                "Here's how I'd use your time:\n\n• 0:00–0:20: Core syntax review (loops & lists)\n• 0:20–1:00: Practice problems (list comprehensions & nested loops)\n• 1:00–1:10: Quick break\n• 1:10–1:50: Timed problem solving\n• 1:50–2:00: Recap & key formulas\n\nWould you like me to save this to your Study Plans?"

            cleanPrompt.contains("recursion") ->
                "Recursion breaks down a big problem into identical smaller sub-problems until reaching a simple base case. Think of opening nesting Russian dolls!"

            cleanPrompt.contains("quiz") ->
                "I've generated a 4-question quiz on your topic! Head over to the Study tab to test your recall."

            else ->
                "I'm here to help with your university studies! You can ask me to explain concepts, build structured study plans, or test your knowledge."
        }

        return ChatMessage(
            text = responseText,
            isUser = false
        )
    }

    override suspend fun answerCampusQuestion(
        question: String,
        retrievedDocuments: List<CampusDocument>
    ): ChatMessage {
        delay(700)

        if (retrievedDocuments.isEmpty()) {
            return ChatMessage(
                text = "I couldn't find that information in the campus knowledge base.",
                isUser = false,
                isCampusQuery = true,
                sources = emptyList(),
                isUngroundedFallback = true
            )
        }

        val primaryDoc = retrievedDocuments.first()
        val sourceTitles = retrievedDocuments.map { it.title }

        val answerText = buildString {
            append("Based on campus records:\n\n")
            append(primaryDoc.content)
            if (retrievedDocuments.size > 1) {
                append("\n\nAdditional relevant details:\n")
                retrievedDocuments.drop(1).forEach { doc ->
                    append("• ${doc.title}: ${doc.content}\n")
                }
            }
        }

        return ChatMessage(
            text = answerText,
            isUser = false,
            isCampusQuery = true,
            sources = sourceTitles
        )
    }
}
