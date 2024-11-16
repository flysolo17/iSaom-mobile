package com.ketchupzzz.isaom.presentation.main.gaming

import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.games.GameSubmission
import com.ketchupzzz.isaom.models.games.Games
import com.ketchupzzz.isaom.models.games.Levels


data class GamingState(
    val isLoading : Boolean = false,
    val levels : List<Levels> = emptyList(),
    val errors : String ? = null,
    val users : Users ? = null,
    val timer : Int  = 0,
    val timerFinish : Boolean = false,
    val currentLevel : Int  = 0,
    val answers : String = "",
    val answerIndex : List<Int> = emptyList(),
    val answerSheet : Map<String,String> = mapOf(),
    val score : Int = 0,
    val isSubmitting : Boolean = false,
    val submitted : String? = null,

)