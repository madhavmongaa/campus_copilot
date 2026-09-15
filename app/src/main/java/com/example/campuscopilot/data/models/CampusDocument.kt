package com.example.campuscopilot.data.models

enum class CampusCategory(val displayName: String) {
    FACILITIES("Facilities"),
    CLUBS("Clubs"),
    SPORTS("Sports"),
    ACADEMICS("Academics"),
    EVENTS("Events"),
    LIBRARY("Library"),
    CAFETERIA("Cafeteria"),
    HOSTEL("Hostel"),
    TRANSPORT("Transport")
}

data class CampusDocument(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val category: CampusCategory,
    val content: String,
    val tags: List<String> = emptyList()
)
