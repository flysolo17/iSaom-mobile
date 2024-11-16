package com.ketchupzzz.isaom.models.sections

import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.subject.Subjects


data class SectionWithSubjectsAndStudents(
    val sections: Sections? = null,
    val subjects : List<Subjects> = emptyList(),
    val students : List<Users> = emptyList()
)
