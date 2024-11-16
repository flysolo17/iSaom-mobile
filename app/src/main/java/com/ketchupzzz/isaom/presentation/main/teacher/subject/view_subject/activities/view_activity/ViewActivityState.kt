package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity

import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.models.submissions.AnswersByQuestion
import com.ketchupzzz.isaom.models.submissions.SubmissionWithStudent
import com.ketchupzzz.isaom.models.submissions.Submissions

data class ViewActivityState(
    val isLoading : Boolean = false,
    val activity : Activity? = null,
    val questions : List<Question> = emptyList(),
    val errors : String ? = null,
    val messages : String? = null,
    val submissionWithStudent: List<SubmissionWithStudent> = emptyList(),
    val answersByQuestion: List<AnswersByQuestion> = emptyList()
)
