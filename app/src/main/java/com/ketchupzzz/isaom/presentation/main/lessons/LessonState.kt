package com.ketchupzzz.isaom.presentation.main.lessons

import com.ketchupzzz.isaom.models.SectionedLessons
import com.ketchupzzz.isaom.models.SignLanguageLesson
import com.ketchupzzz.isaom.repository.lessons.LessonAccount

data class LessonState(
    val isLoading : Boolean = false,
    val lessons : List<SignLanguageLesson> = emptyList(),
    val errors : String ? = null,
    val lessonAccount: LessonAccount ? = null
)
