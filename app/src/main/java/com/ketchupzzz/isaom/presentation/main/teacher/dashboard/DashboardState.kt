package com.ketchupzzz.isaom.presentation.main.teacher.dashboard

import com.ketchupzzz.isaom.models.sections.SectionWithSubjects
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.sections.SectionWithStudents
import com.ketchupzzz.isaom.models.sections.Sections

data class DashboardState(
    val isLoading : Boolean = false,
    val sections : List<Sections> = emptyList(),
    val users : Users ? = null,
    val errors : String? = null,
)
