package com.ketchupzzz.isaom.presentation.main.lessons



sealed interface LessonEvents {
    data object OnGetAllLessons :LessonEvents
}