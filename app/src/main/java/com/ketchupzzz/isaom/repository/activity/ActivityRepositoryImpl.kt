package com.ketchupzzz.isaom.repository.activity

import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.utils.generateRandomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ActivityRepositoryImpl(private val firestore: FirebaseFirestore,private val storage : FirebaseStorage): ActivityRepository {


    override fun createActivity(activity: Activity, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(ACTIVITY_COLLECTION)
            .document(activity.id ?: generateRandomString())
            .set(activity)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Created"))
                } else {
                    result.invoke(UiState.Error("Unknown error!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Error(it.message.toString()))
            }
    }


    override suspend fun getActivityByID(id: String, result: (UiState<Activity?>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(ACTIVITY_COLLECTION)
            .document(id)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(UiState.Success(it.toObject(Activity::class.java)))
                }
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }

    override suspend fun getAllActivities(subjectID: String, result: (UiState<List<Activity>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(ACTIVITY_COLLECTION)
            .whereEqualTo("subjectID",subjectID)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Activity::class.java)))
                }
            }
    }

    override suspend fun deleteActivity(activityID: String, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        try {
            val batch = firestore.batch()
            val mainRef = firestore.collection(ACTIVITY_COLLECTION).document(activityID)
            batch.delete(mainRef)

            val questions = mainRef
                .collection(QUESTION_COLLECTION)
                .get()
                .await()

            questions.forEach {
                batch.delete(it.reference)
            }

            batch.commit().await()
            result.invoke(UiState.Success("Successfully Deleted"))
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.message.toString()))
        }
    }
    override suspend fun updateActivity(activity: Activity, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(ACTIVITY_COLLECTION)
            .document(activity.id!!)
            .set(activity.open)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Updated!"))
                } else {
                    result.invoke(UiState.Success("Unkown Error!"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Error(it.message.toString()))
            }
    }

    override suspend fun updateLock(activity: Activity, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(ACTIVITY_COLLECTION)
            .document(activity.id!!)
            .update("open",!activity.open)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Updated!"))
                } else {
                    result.invoke(UiState.Success("Unkown Error!"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Error(it.message.toString()))
            }
    }

    override suspend fun getQuestionByActivityID(
        activityID: String,
        result: (UiState<List<Question>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(ACTIVITY_COLLECTION)
            .document(activityID)
            .collection(QUESTION_COLLECTION)
            .orderBy("createdAt",Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Question::class.java)))
                }
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }

    override suspend fun createQuestion(
        actvityID: String,
        question: Question,
        uri : Uri?,
        result: (UiState<String>) -> Unit
    ) {
        try {
            result.invoke(UiState.Loading)
            if (uri != null) {
                val storageRef = storage.reference
                    .child(ACTIVITY_COLLECTION)
                    .child(QUESTION_COLLECTION)
                    .child("${generateRandomString(10)}.${MimeTypeMap.getFileExtensionFromUrl(uri.toString())}")
                val downloadUri = withContext(Dispatchers.IO) {
                    storageRef.putFile(uri).await()
                    storageRef.downloadUrl.await()
                }
                question.image = downloadUri.toString()
            }

            firestore.collection(ACTIVITY_COLLECTION)
                .document(actvityID)
                .collection(QUESTION_COLLECTION)
                .document(question.id!!)
                .set(question)
                .await()
            result.invoke(UiState.Success("Successfully Added"))
        }catch (e : Exception) {
            result.invoke(UiState.Error(e.message.toString()))
        }
    }

    override suspend fun deleteQuestion(
        activityID: String,
        question: Question,
        result: (UiState<String>) -> Unit
    ) {
        try {
            result.invoke(UiState.Loading)
            if (!question.image.isNullOrEmpty()) {
                storage.getReferenceFromUrl(question.image!!).delete().await()
            }
            firestore.collection(ACTIVITY_COLLECTION)
                .document(activityID)
                .collection(QUESTION_COLLECTION)
                .document(question.id!!)
                .delete()
                .await()
            result.invoke(UiState.Success("Successfully deleted!"))
        } catch (e : Exception) {
            result.invoke(UiState.Error(e.message.toString()))
        }
    }



    companion object {
        const val ACTIVITY_COLLECTION = "activities"
        const val QUESTION_COLLECTION = "questions"
    }
}