package com.example.campuscopilot.domain.repositories

import com.example.campuscopilot.data.models.CampusCategory
import com.example.campuscopilot.data.models.CampusDocument

interface CampusRepository {
    suspend fun getAllDocuments(): List<CampusDocument>
    suspend fun getDocumentsByCategory(category: CampusCategory): List<CampusDocument>
    suspend fun searchDocuments(query: String): List<CampusDocument>
}
