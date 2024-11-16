package com.ketchupzzz.isaom.presentation.main.game

import com.ketchupzzz.isaom.models.WordTranslate
import com.ketchupzzz.isaom.models.games.Games


data class GameState(
    val isLoading : Boolean  = false,
    val errors : String ? = null,
    val games : List<Games> = emptyList()
)