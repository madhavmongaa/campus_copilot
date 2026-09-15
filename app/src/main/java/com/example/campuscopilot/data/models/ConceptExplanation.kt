package com.example.campuscopilot.data.models

data class ConceptExplanation(
    val id: String = java.util.UUID.randomUUID().toString(),
    val concept: String,
    val difficulty: String,
    val simpleExplanation: String,
    val example: String,
    val commonMistakes: String,
    val quickTestQuestion: String
)
