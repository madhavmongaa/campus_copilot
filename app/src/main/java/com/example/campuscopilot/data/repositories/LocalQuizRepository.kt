package com.example.campuscopilot.data.repositories

import com.example.campuscopilot.data.models.QuizResult
import com.example.campuscopilot.domain.repositories.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalQuizRepository : QuizRepository {

    private val _quizResults = MutableStateFlow<List<QuizResult>>(emptyList())
    override val quizResults: StateFlow<List<QuizResult>> = _quizResults.asStateFlow()

    override suspend fun saveQuizResult(result: QuizResult) {
        val current = _quizResults.value.toMutableList()
        current.add(0, result)
        _quizResults.value = current
    }

    override suspend fun getQuizResultsForTopic(topic: String): List<QuizResult> {
        return _quizResults.value.filter { it.topic.equals(topic, ignoreCase = true) }
    }
}
