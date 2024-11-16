package com.ketchupzzz.isaom.presentation.main.teacher.sections.view_section




sealed interface ViewSectionEvents {
    data class getSectionWithSubjects(
        val sectionID : String
    ) : ViewSectionEvents
}