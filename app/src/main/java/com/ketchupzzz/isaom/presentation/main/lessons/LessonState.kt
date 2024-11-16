package com.ketchupzzz.isaom.presentation.main.lessons

import com.ketchupzzz.isaom.models.SignLanguageLesson

data class LessonState(
    val isLoading : Boolean = false,
    val lessons : List<SignLanguageLesson> = emptyList(),
    val errors : String ? = null
)
