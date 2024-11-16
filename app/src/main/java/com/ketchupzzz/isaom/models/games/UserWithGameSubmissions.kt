package com.ketchupzzz.isaom.models.games

import com.ketchupzzz.isaom.models.Users


data class UserWithGameSubmissions(
    val user : Users,
    val submission: List<GameSubmission>,
    val totalScore : Int,
)