package com.ketchupzzz.isaom.presentation.main.translator

import com.ketchupzzz.isaom.models.SourceAndTargets
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.history.TranslatorHistory

data class TranslatorState(
    val isLoading : Boolean = false,
    val text : String = "",
    val translation : String ? = null,
    val source : SourceAndTargets = SourceAndTargets.ENGLISH,
    val target : SourceAndTargets = SourceAndTargets.ILOCANO,
    val errors : String ? = null,
    val users : Users ? = null,
    val history: List<TranslatorHistory> = emptyList()
)