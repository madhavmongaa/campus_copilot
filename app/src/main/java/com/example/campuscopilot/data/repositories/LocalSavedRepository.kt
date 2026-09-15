package com.example.campuscopilot.data.repositories

import com.example.campuscopilot.data.models.SavedCategory
import com.example.campuscopilot.data.models.SavedItem
import com.example.campuscopilot.domain.repositories.SavedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalSavedRepository : SavedRepository {

    private val initialItems = listOf(
        SavedItem(
            id = "demo_1",
            title = "Python — Loops & Lists 2-Hour Plan",
            subtitle = "Study Plan • Tomorrow • Beginner",
            category = SavedCategory.PLANS,
            content = "Time Blocks:\n• 0:00–0:20 Concept review\n• 0:20–1:00 Practice problems\n• 1:00–1:10 Break\n• 1:10–1:50 Edge case traps\n• 1:50–2:00 Final revision",
            dateSaved = "Today"
        ),
        SavedItem(
            id = "demo_2",
            title = "Recursion & Base Cases Breakdown",
            subtitle = "Concept Answer • Intermediate",
            category = SavedCategory.ANSWERS,
            content = "Recursion is when a function calls itself to solve smaller sub-problems until reaching a simple base case.",
            dateSaved = "Yesterday"
        ),
        SavedItem(
            id = "demo_3",
            title = "Python Control Flow Quiz (4/4)",
            subtitle = "Quiz Result • Score: 100%",
            category = SavedCategory.QUIZZES,
            content = "Mastered: range() syntax, continue keyword, infinite loops, and `in` membership operator.",
            dateSaved = "2 days ago"
        ),
        SavedItem(
            id = "demo_4",
            title = "Campus Coding & Robotics Labs",
            subtitle = "Campus Info • Grounded",
            category = SavedCategory.ANSWERS,
            content = "Student Innovation Lab located in Engineering Block B Room 302 equipped with 3D printers and high-performance workstations.",
            dateSaved = "3 days ago"
        )
    )

    private val _savedItems = MutableStateFlow<List<SavedItem>>(initialItems)
    override val savedItems: StateFlow<List<SavedItem>> = _savedItems.asStateFlow()

    override suspend fun saveItem(item: SavedItem) {
        val current = _savedItems.value.toMutableList()
        current.add(0, item)
        _savedItems.value = current
    }

    override suspend fun deleteItem(id: String) {
        _savedItems.value = _savedItems.value.filter { it.id != id }
    }

    override suspend fun getFilteredItems(category: SavedCategory): List<SavedItem> {
        return if (category == SavedCategory.ALL) {
            _savedItems.value
        } else {
            _savedItems.value.filter { it.category == category }
        }
    }

    override suspend fun clearAll() {
        _savedItems.value = emptyList()
    }
}
