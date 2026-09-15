package com.example.campuscopilot.data.models

data class QuizQuestion(
    val id: String = java.util.UUID.randomUUID().toString(),
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)

data class Quiz(
    val id: String = java.util.UUID.randomUUID().toString(),
    val topic: String,
    val difficulty: String,
    val questions: List<QuizQuestion>
)

data class UserAnswer(
    val questionId: String,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean
)

data class QuizResult(
    val id: String = java.util.UUID.randomUUID().toString(),
    val topic: String,
    val difficulty: String,
    val score: Int,
    val totalQuestions: Int,
    val userAnswers: List<UserAnswer>,
    val timestamp: Long = System.currentTimeMillis()
)
