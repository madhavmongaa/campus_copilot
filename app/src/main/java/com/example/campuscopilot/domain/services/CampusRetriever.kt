package com.example.campuscopilot.domain.services

import com.example.campuscopilot.data.models.CampusDocument

interface CampusRetriever {
    suspend fun retrieveRelevantDocuments(query: String): List<CampusDocument>
}
