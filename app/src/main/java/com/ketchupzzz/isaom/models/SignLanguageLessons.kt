package com.ketchupzzz.isaom.models

import java.util.Date

data class SectionedLessons(
    val difficulty: Difficulty ? = null,
    val lessons : List<SignLanguageLesson>,
)



data class SignLanguageLesson(
    val id: String ? = null,
    val title: String ? = null,
    val desc: String ? = null,
    val videoId: String ? = null,
    val dificulty: Difficulty ? = null,
    val createdAt: Date  ? = null,
)


enum class Difficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}