package com.example.campuscopilot.data.models

enum class SavedCategory {
    ALL,
    PLANS,
    ANSWERS,
    QUIZZES
}

data class SavedItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val subtitle: String,
    val category: SavedCategory,
    val content: String,
    val dateSaved: String = "Today",
    val timestamp: Long = System.currentTimeMillis()
)
