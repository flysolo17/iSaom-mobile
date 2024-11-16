package com.ketchupzzz.isaom.presentation.main.leaderboard

import androidx.compose.ui.graphics.drawscope.Stroke
import com.ketchupzzz.isaom.models.games.GameSubmission
import com.ketchupzzz.isaom.models.games.UserWithGameSubmissions


data class LeaderboardState(
    val isLoading : Boolean = false,
    val submissions : List<UserWithGameSubmissions> = emptyList(),
    val errors : String ? = null
)