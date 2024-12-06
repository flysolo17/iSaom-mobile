package com.ketchupzzz.isaom.repository.lessons

import java.util.Date


    data class LessonAccount(
        val studentID : String ? = null,
        val lessons : List<String> = emptyList(),
        val createdAt : Date = Date(),
        val updatedAt : Date = Date()
    )