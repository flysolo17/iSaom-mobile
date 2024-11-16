package com.ketchupzzz.isaom.presentation.main.students.home

import com.ketchupzzz.isaom.models.SourceAndTargets
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.subject.Subjects

sealed interface StudentHomeEvents {
    data class OnSetUsers(val users: Users ?) : StudentHomeEvents
    data class OnGetSubjects(val studentID : String) : StudentHomeEvents
    data class OnFindSubjects(
        val code : String
    ) : StudentHomeEvents
    data class UpdateSubjectFound(
        val subjects: Subjects ?
    ) : StudentHomeEvents
    data class OnJoinSubject(
        val studentID : String,
        val subjectID : String,
    ) : StudentHomeEvents
}