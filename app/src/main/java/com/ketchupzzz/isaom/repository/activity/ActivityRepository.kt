package com.ketchupzzz.isaom.repository.activity

import android.net.Uri
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.activities.Question

interface ActivityRepository {

    fun createActivity(
        activity: Activity,
        result : (UiState<String>) -> Unit
    )

    suspend fun getActivityByID(id : String, result: (UiState<Activity?>) -> Unit)
    suspend fun  getAllActivities(
        subjectID : String,
        result: (UiState<List<Activity>>) -> Unit
    )


    suspend fun deleteActivity(activityID : String, result: (UiState<String>) -> Unit)
    suspend fun updateActivity(activity: Activity,result: (UiState<String>) -> Unit)
    suspend fun updateLock(activity: Activity,result: (UiState<String>) -> Unit)

    suspend fun getQuestionByActivityID(activityID : String,result: (UiState<List<Question>>) -> Unit)
    suspend fun createQuestion(actvityID : String,question: Question,uri : Uri ?,result: (UiState<String>) -> Unit)
    suspend fun deleteQuestion(activityID: String,question: Question,result: (UiState<String>) -> Unit)

}

