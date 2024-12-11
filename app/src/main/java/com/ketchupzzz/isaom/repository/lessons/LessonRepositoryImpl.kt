package com.ketchupzzz.isaom.repository.lessons

import android.util.Log
import coil.util.CoilUtils.result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ketchupzzz.isaom.models.Difficulty
import com.ketchupzzz.isaom.models.SectionedLessons
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.SignLanguageLesson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import java.util.Date

const val SIGN_LANGUAGE_LESSONS = "sign-language-lessons"
const val LESSON_ACCOUNT = "lessons-account"
class LessonRepositoryImpl(
    private  val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth
): LessonRepository {


    override suspend fun getAllLessons(result: (UiState<List<SignLanguageLesson>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(SIGN_LANGUAGE_LESSONS)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(SignLanguageLesson::class.java)))
                }
            }
    }

    override suspend fun getLessonAccount(result: (UiState<LessonAccount>) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            result.invoke(UiState.Error("No user account"))
            return
        }
        firestore.collection("lesson_accounts")
            .document(uid)
            .addSnapshotListener { documentSnapshot, e ->
                if (e != null) {
                    result.invoke(UiState.Error(e.localizedMessage ?: "An unknown error occurred"))
                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Document exists; convert it to a LessonAccount
                    val lessonAccount = documentSnapshot.toObject(LessonAccount::class.java)
                    if (lessonAccount != null) {
                        result.invoke(UiState.Success(lessonAccount))
                    } else {
                        result.invoke(UiState.Error("Failed to parse lesson account"))
                    }
                } else {
                    val newLessonAccount = LessonAccount(
                        studentID = uid,
                        lessons = emptyList(),
                        createdAt = Date(),
                        updatedAt = Date()
                    )

                    firestore.collection("lesson_accounts")
                        .document(uid)
                        .set(newLessonAccount)
                        .addOnSuccessListener {
                            result.invoke(UiState.Success(newLessonAccount))
                        }
                        .addOnFailureListener { exception ->
                            result.invoke(UiState.Error(exception.localizedMessage ?: "Failed to create lesson account"))
                        }
                }
            }
    }





    override suspend fun updateLesson(lessonID: String): Result<String> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User not authenticated"))
        return try {
            val updateResult = firestore.collection("lesson_accounts")
                .document(uid)
                .update("lessons", FieldValue.arrayUnion(lessonID),"updatedAt",Date())
                .await()
            Result.success("Lesson updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}