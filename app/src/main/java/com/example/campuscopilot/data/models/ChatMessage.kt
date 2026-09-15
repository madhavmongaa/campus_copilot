package com.example.campuscopilot.data.models

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isCampusQuery: Boolean = false,
    val sources: List<String> = emptyList(),
    val isUngroundedFallback: Boolean = false
)
