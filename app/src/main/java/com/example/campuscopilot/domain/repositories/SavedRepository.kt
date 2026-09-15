package com.example.campuscopilot.domain.repositories

import com.example.campuscopilot.data.models.SavedCategory
import com.example.campuscopilot.data.models.SavedItem
import kotlinx.coroutines.flow.StateFlow

interface SavedRepository {
    val savedItems: StateFlow<List<SavedItem>>
    suspend fun saveItem(item: SavedItem)
    suspend fun deleteItem(id: String)
    suspend fun getFilteredItems(category: SavedCategory): List<SavedItem>
    suspend fun clearAll()
}
