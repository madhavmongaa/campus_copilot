package com.example.campuscopilot.domain.repositories

import com.example.campuscopilot.data.models.StudyPlan
import kotlinx.coroutines.flow.StateFlow

interface StudyPlanRepository {
    val studyPlans: StateFlow<List<StudyPlan>>
    suspend fun savePlan(plan: StudyPlan)
    suspend fun toggleBlockCompletion(planId: String, blockId: String)
    suspend fun getPlanById(id: String): StudyPlan?
    suspend fun deletePlan(id: String)
}
