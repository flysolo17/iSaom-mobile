package com.ketchupzzz.isaom.repository.lessons

import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.SignLanguageLesson

interface LessonRepository {
    fun getAllLessons(result : (UiState<List<SignLanguageLesson>>) -> Unit)
}