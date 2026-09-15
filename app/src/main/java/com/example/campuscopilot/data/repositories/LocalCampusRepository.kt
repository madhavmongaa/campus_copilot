package com.example.campuscopilot.data.repositories

import com.example.campuscopilot.data.models.CampusCategory
import com.example.campuscopilot.data.models.CampusDocument
import com.example.campuscopilot.data.services.MockCampusRetriever
import com.example.campuscopilot.domain.repositories.CampusRepository

class LocalCampusRepository(
    private val retriever: MockCampusRetriever = MockCampusRetriever()
) : CampusRepository {

    override suspend fun getAllDocuments(): List<CampusDocument> {
        return retriever.knowledgeBase
    }

    override suspend fun getDocumentsByCategory(category: CampusCategory): List<CampusDocument> {
        return retriever.knowledgeBase.filter { it.category == category }
    }

    override suspend fun searchDocuments(query: String): List<CampusDocument> {
        return retriever.retrieveRelevantDocuments(query)
    }
}
