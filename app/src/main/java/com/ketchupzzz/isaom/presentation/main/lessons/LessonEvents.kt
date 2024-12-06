package com.ketchupzzz.isaom.presentation.main.lessons



sealed interface LessonEvents {
    data object OnGetAllLessons :LessonEvents
    data object OnGetLessonAccount : LessonEvents
    data class OnUpdate(val lessonID : String) : LessonEvents
}