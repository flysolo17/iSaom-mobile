package com.ketchupzzz.isaom.models.sections

import com.ketchupzzz.isaom.models.subject.Subjects

data class SectionWithSubjects(
    val sections: Sections? = null,
    val subjects : List<Subjects> = emptyList()
)
