package com.ketchupzzz.isaom.models.sections

import com.ketchupzzz.isaom.models.Users


data class SectionWithStudents(
    val sections: Sections ? = null,
    val students : List<Users> = emptyList()
)