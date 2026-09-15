package com.example.campuscopilot.data.models

data class StudyBlock(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timeRange: String,
    val activity: String,
    var isCompleted: Boolean = false
)

data class StudyPlan(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val subject: String,
    val topic: String,
    val availableTime: String,
    val deadline: String,
    val difficulty: String,
    val priority: String,
    val blocks: List<StudyBlock>,
    val createdAt: Long = System.currentTimeMillis()
)
