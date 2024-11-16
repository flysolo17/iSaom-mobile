package com.ketchupzzz.isaom.repository.submission

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.submissions.SubmissionWithStudent
import com.ketchupzzz.isaom.models.submissions.Submissions
import com.ketchupzzz.isaom.repository.auth.USERS_COLLECTION
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.utils.generateRandomString
import kotlinx.coroutines.tasks.await


class SubmissionRepositoryImpl(
    private val firestore: FirebaseFirestore
) : SubmissionRepository {


    override suspend fun createSubmission(submissions: Submissions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(SUBMISSION_COLLECTION)
            .document(submissions.id?: generateRandomString(20))
            .set(submissions)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result(UiState.Success("Submitted Successfully!"))
                } else {
                    result(UiState.Error("Error submitting data!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Error(it.message.toString()))
            }
    }

    override suspend fun getAllSubmissionsByStudentID(
        subjectID : String,
        studentID: String,

        result: (UiState<List<Submissions>>) -> Unit,
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(SUBMISSION_COLLECTION)
            .whereEqualTo("subjectID",subjectID)
            .whereEqualTo("studentID",studentID)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    val data = it.toObjects(Submissions::class.java)
                    result.invoke(UiState.Success(data))
                }
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }

    override suspend fun getSubmissionsByActivityID(
        activityID: String,
        result: (UiState<List<SubmissionWithStudent>>) -> Unit,
    ) {
        try {
            result.invoke(UiState.Loading)

            val snapshot = firestore.collection(SUBMISSION_COLLECTION)
                .whereEqualTo("activityID", activityID)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val submissions = snapshot.toObjects(Submissions::class.java)
            val submissionWithStudents = submissions.map {
                val student = firestore.collection(USERS_COLLECTION)
                    .document(it.studentID ?: "")
                    .get()
                    .await()
                    .toObject(Users::class.java)

                SubmissionWithStudent(
                    submissions = it,
                    student = student
                )
            }
            result.invoke(UiState.Success(submissionWithStudents))
        } catch (e : Exception) {
            Log.e(SUBMISSION_COLLECTION,e.message.toString(),e)
            result.invoke(UiState.Error(e.message.toString()))
        }

    }


    companion object {

        const val SUBMISSION_COLLECTION = "submissions"
    }
}