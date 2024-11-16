package com.ketchupzzz.isaom.presentation.main.teacher.sections.view_section

import com.ketchupzzz.isaom.models.sections.Sections
import com.ketchupzzz.isaom.models.subject.Subjects


data class ViewSectionState(
    val isLoading : Boolean = false,
    val sections: Sections ? = null,
    val subjects : List<Subjects> = emptyList(),
    val errors : String ? = null
)