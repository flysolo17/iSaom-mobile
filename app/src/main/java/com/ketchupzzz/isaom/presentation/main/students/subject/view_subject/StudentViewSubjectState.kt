package com.ketchupzzz.isaom.presentation.main.students.subject.view_subject

import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.module.Modules
import com.ketchupzzz.isaom.models.submissions.Submissions
import dagger.Module

data class StudentViewSubjectState(
    val isLoading : Boolean = false,
    val modules : List<Modules> = emptyList(),
    val activities : List<Activity> = emptyList(),
    val submissions : List<Submissions> = emptyList(),
    val errors : String? = null,
    val subject : Subjects ? = null,
    val isGettingModules : Boolean = false,
    val isGettingActivities : Boolean = false,
    val isGettingSubmissions : Boolean = false
)
