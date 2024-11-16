package com.ketchupzzz.isaom.repository.submission

import com.ketchupzzz.isaom.models.submissions.SubmissionWithStudent
import com.ketchupzzz.isaom.models.submissions.Submissions
import com.ketchupzzz.isaom.utils.UiState

interface SubmissionRepository {
    suspend fun createSubmission(submissions: Submissions,result: (UiState<String>) -> Unit)
    suspend fun getAllSubmissionsByStudentID(
        subjectID : String,
        studentID : String,
        result: (UiState<List<Submissions>>) -> Unit
    )
    suspend fun getSubmissionsByActivityID(
        activityID : String,
        result: (UiState<List<SubmissionWithStudent>>) -> Unit
    )
}