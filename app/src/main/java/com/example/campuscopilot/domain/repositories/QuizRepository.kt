package com.example.campuscopilot.domain.repositories

import com.example.campuscopilot.data.models.QuizResult
import kotlinx.coroutines.flow.StateFlow

interface QuizRepository {
    val quizResults: StateFlow<List<QuizResult>>
    suspend fun saveQuizResult(result: QuizResult)
    suspend fun getQuizResultsForTopic(topic: String): List<QuizResult>
}
