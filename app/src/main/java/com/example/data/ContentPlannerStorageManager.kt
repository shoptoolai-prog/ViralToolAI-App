package com.example.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf

/**
 * SHOPTOOLAI Phase 10E — Local Content Planner Storage Manager
 * Stores local plans, weekly/monthly roadmaps, goal checkbox states, and saved daily ideas.
 */

data class SavedContentPlan(
    val id: String,
    val productUrl: String,
    val productName: String,
    val category: String,
    val dateSaved: String,
    val weeklyFocus: String,
    val totalIdeasCount: Int
)

object ContentPlannerStorageManager {
    private val _savedPlans = mutableStateListOf<SavedContentPlan>()
    private val _completedGoals = mutableStateMapOf<String, Boolean>()
    private val _savedIdeas = mutableStateListOf<String>()

    fun getSavedPlans(): List<SavedContentPlan> = _savedPlans.toList()

    fun isGoalCompleted(goalId: String): Boolean = _completedGoals[goalId] ?: false

    fun toggleGoal(goalId: String): Boolean {
        val current = isGoalCompleted(goalId)
        _completedGoals[goalId] = !current
        return !current
    }

    fun isIdeaSaved(ideaTitle: String): Boolean = _savedIdeas.contains(ideaTitle)

    fun toggleSaveIdea(ideaTitle: String): Boolean {
        return if (_savedIdeas.contains(ideaTitle)) {
            _savedIdeas.remove(ideaTitle)
            false
        } else {
            _savedIdeas.add(ideaTitle)
            true
        }
    }

    fun savePlan(
        productUrl: String,
        productName: String,
        category: String,
        weeklyFocus: String,
        totalIdeasCount: Int
    ): Boolean {
        val dateStr = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date())
        val existingIndex = _savedPlans.indexOfFirst { it.productUrl.equals(productUrl, ignoreCase = true) }
        val plan = SavedContentPlan(
            id = if (existingIndex != -1) _savedPlans[existingIndex].id else java.util.UUID.randomUUID().toString(),
            productUrl = productUrl,
            productName = productName,
            category = category,
            dateSaved = dateStr,
            weeklyFocus = weeklyFocus,
            totalIdeasCount = totalIdeasCount
        )

        if (existingIndex != -1) {
            _savedPlans.removeAt(existingIndex)
        }
        _savedPlans.add(0, plan)
        return true
    }

    fun isPlanSaved(productUrl: String): Boolean {
        return _savedPlans.any { it.productUrl.equals(productUrl, ignoreCase = true) }
    }

    fun removePlan(id: String) {
        _savedPlans.removeAll { it.id == id }
    }
}
