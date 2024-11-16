package com.ketchupzzz.isaom.models

import java.util.Date

data class SignLanguageLesson(
    val id: String ? = null,
    val title: String ? = null,
    val desc: String ? = null,
    val videoId: String ? = null,
    val createdAt: Date  ? = null,
)
