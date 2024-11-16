package com.ketchupzzz.isaom.presentation.main.students.subject.activities.view

import com.ketchupzzz.isaom.models.subject.activities.Question


data class StudentViewActivityState(
    val isLoading : Boolean  = false,
    val questions : List<Question> = emptyList(),
    val errors : String ? = null,
    val answers: Map<String, String> = hashMapOf(),
    val isSubmitted : String ? = null
)