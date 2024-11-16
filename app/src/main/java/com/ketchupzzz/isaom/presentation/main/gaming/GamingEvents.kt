package com.ketchupzzz.isaom.presentation.main.gaming

import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.games.GameSubmission
import com.ketchupzzz.isaom.models.games.Games


sealed interface GamingEvents {
    data class OnSetUsers(
        val users : Users ?
    ) : GamingEvents
    data class OnGetLevels(
        val game : Games
    ) : GamingEvents
    data class OnStartTimer(val time : Int) : GamingEvents
    data class OnAnswerChanged(val text : String,val index : Int) : GamingEvents
    data object OnReset : GamingEvents
    data class OnFinish(val gameSubmission: GameSubmission,val navHostController: NavHostController) : GamingEvents
    data class OnAddAnser(val answers : Pair<String,String>) : GamingEvents
    data class OnIncrementScore(val points : Int) : GamingEvents

}