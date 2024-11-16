package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity

import android.net.Uri
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.activities.Question


sealed interface ViewActivityEvents {
    data class OnGetActivityByID(val id : String) : ViewActivityEvents
    data class OnGetQuestionByActivityID(val activityID: String) : ViewActivityEvents
    data class OnSaveQuestion(val actvityID : String,val question : Question,val uri : Uri?,val result : (UiState<String>) -> Unit) :
        ViewActivityEvents
    data class OnEdeleteQuestion(val activityID: String,val question: Question) : ViewActivityEvents
    data class OnGetAllSubmissionsByActivityID(
        val id : String,
        val question: List<Question>
    ) : ViewActivityEvents
}