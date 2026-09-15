package com.example.campuscopilot.data.repositories

import com.example.campuscopilot.data.models.StudyBlock
import com.example.campuscopilot.data.models.StudyPlan
import com.example.campuscopilot.domain.repositories.StudyPlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalStudyPlanRepository : StudyPlanRepository {

    private val initialPlan = StudyPlan(
        id = "initial_python_plan",
        title = "Python — Loops & Lists",
        subject = "Python",
        topic = "Loops & Lists",
        availableTime = "2 hours",
        deadline = "Tomorrow",
        difficulty = "Beginner",
        priority = "High Priority",
        blocks = listOf(
            StudyBlock(timeRange = "0:00 – 0:20", activity = "Concept review", isCompleted = true),
            StudyBlock(timeRange = "0:20 – 1:00", activity = "Practice problems", isCompleted = true),
            StudyBlock(timeRange = "1:00 – 1:10", activity = "Break", isCompleted = false),
            StudyBlock(timeRange = "1:10 – 1:50", activity = "Problems", isCompleted = false),
            StudyBlock(timeRange = "1:50 – 2:00", activity = "Final revision", isCompleted = false)
        )
    )

    private val _studyPlans = MutableStateFlow<List<StudyPlan>>(listOf(initialPlan))
    override val studyPlans: StateFlow<List<StudyPlan>> = _studyPlans.asStateFlow()

    override suspend fun savePlan(plan: StudyPlan) {
        val current = _studyPlans.value.toMutableList()
        current.removeAll { it.id == plan.id }
        current.add(0, plan)
        _studyPlans.value = current
    }

    override suspend fun toggleBlockCompletion(planId: String, blockId: String) {
        val current = _studyPlans.value.map { plan ->
            if (plan.id == planId) {
                val updatedBlocks = plan.blocks.map { block ->
                    if (block.id == blockId) {
                        block.copy(isCompleted = !block.isCompleted)
                    } else {
                        block
                    }
                }
                plan.copy(blocks = updatedBlocks)
            } else {
                plan
            }
        }
        _studyPlans.value = current
    }

    override suspend fun getPlanById(id: String): StudyPlan? {
        return _studyPlans.value.find { it.id == id }
    }

    override suspend fun deletePlan(id: String) {
        _studyPlans.value = _studyPlans.value.filter { it.id != id }
    }
}
