package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject

import android.content.Context
import android.net.Uri
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.module.Modules


sealed interface ViewSubjectEvents {
    data class OnGetSubjectByID(val id : String) : ViewSubjectEvents
    data class OnGetModulesBySubjectID(val subjectID: String) : ViewSubjectEvents
    data class OnGetActivitiesBySubjectID(val subjectID: String) : ViewSubjectEvents
    data class OnCreateModule(val module : Modules,val uri : Uri,val result: (UiState<String>) -> Unit):
        ViewSubjectEvents

    data class OnDeleteSubject(val subjects: Subjects) : ViewSubjectEvents
    data class OnDeleteModule(val module : Modules,val context : Context) : ViewSubjectEvents
    data class UpdateLock(val moduleID : String,val lock : Boolean,val context: Context) :
        ViewSubjectEvents

    data class OnCreateActivity(val activity: Activity, val result  : (UiState<String>) -> Unit) :
        ViewSubjectEvents

    data class updateActivityLock(val activity : Activity,val context: Context) : ViewSubjectEvents
    data class deleteActivity(val activityID: String,val context: Context) : ViewSubjectEvents
}