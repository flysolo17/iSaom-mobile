package com.ketchupzzz.isaom.presentation.main.students.subject.view_subject

sealed interface StudentViewSubjectEvent {
    data class OnGetSubjectModules(val subjectID : String) : StudentViewSubjectEvent
    data class OnGetSubjectActivities(val subjectID : String) : StudentViewSubjectEvent

    data class OnGetSubject(
        val subjectID: String,

    ) : StudentViewSubjectEvent

    data object OnGetSubmissions : StudentViewSubjectEvent
}