package com.ketchupzzz.isaom.repository.lessons

import com.ketchupzzz.isaom.models.SectionedLessons
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.SignLanguageLesson

interface LessonRepository {
  suspend  fun getAllLessons(result : (UiState<List<SignLanguageLesson>>) -> Unit)


    suspend fun getLessonAccount() : Result<LessonAccount>
    suspend fun updateLesson(lessonID : String) : Result<String>
}