package com.ketchupzzz.isaom.presentation.main.class_record

import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.models.submissions.Submissions


data class ClassRecordState(
    val isLoading :Boolean = false,
    val subjects : List<Subjects> = emptyList(),
    val submissions : List<Submissions> = emptyList(),

)