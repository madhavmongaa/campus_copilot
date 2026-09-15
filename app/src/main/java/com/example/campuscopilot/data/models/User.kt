package com.example.campuscopilot.data.models

data class User(
    val name: String = "Madhav",
    val role: String = "Computer Science Undergrad",
    val university: String = "Tech University",
    val avatarUrl: String? = null,
    val studyPlansCount: Int = 8,
    val quizzesCount: Int = 15,
    val questionsAskedCount: Int = 34
)
