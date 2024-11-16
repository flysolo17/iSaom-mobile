package com.ketchupzzz.isaom.models.subject

import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.module.Modules

data class SubjectWithModulesAndActivities(
    val subjects: Subjects? = null,
    val modules : List<Modules> = emptyList(),
    val activities : List<Activity> = emptyList()
)
