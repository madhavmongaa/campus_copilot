package com.example.campuscopilot.domain.services

import com.example.campuscopilot.data.models.ChatMessage
import com.example.campuscopilot.data.models.ConceptExplanation
import com.example.campuscopilot.data.models.Quiz
import com.example.campuscopilot.data.models.StudyPlan
import com.example.campuscopilot.data.models.CampusDocument

interface AIService {
    suspend fun generateStudyPlan(
        subject: String,
        topic: String,
        availableTime: String,
        deadline: String,
        difficulty: String
    ): StudyPlan

    suspend fun explainConcept(
        concept: String,
        difficulty: String
    ): ConceptExplanation

    suspend fun generateQuiz(
        topic: String,
        numQuestions: Int,
        difficulty: String
    ): Quiz

    suspend fun chat(
        prompt: String,
        conversationHistory: List<ChatMessage> = emptyList()
    ): ChatMessage

    suspend fun answerCampusQuestion(
        question: String,
        retrievedDocuments: List<CampusDocument>
    ): ChatMessage
}
