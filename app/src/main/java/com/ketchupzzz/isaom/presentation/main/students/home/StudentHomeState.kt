package com.ketchupzzz.isaom.presentation.main.students.home

import com.ketchupzzz.isaom.models.SourceAndTargets
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.subject.Subjects


data class StudentHomeState(
    val isLoading : Boolean = false,
    val error : String ? = null,
    val subjects: List<Subjects> = emptyList(),
    val users : Users ? = null,

    val subjectFound : Subjects ? = null,

    val joining : Boolean = false
)