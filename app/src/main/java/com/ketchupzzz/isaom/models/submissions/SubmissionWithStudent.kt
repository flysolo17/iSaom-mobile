package com.ketchupzzz.isaom.models.submissions

import com.ketchupzzz.isaom.models.Users


data class SubmissionWithStudent(
    val submissions: Submissions ? = null,
    val student : Users ? = null,
)