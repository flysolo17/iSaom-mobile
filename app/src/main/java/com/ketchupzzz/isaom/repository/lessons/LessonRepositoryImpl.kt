package com.ketchupzzz.isaom.repository.lessons

import android.util.Log
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



    override suspend fun getLessonAccount(): Result<LessonAccount> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User not authenticated"))
        return try {
            val task = CompletableDeferred<Result<LessonAccount>>()
            firestore.collection("lesson_accounts")
                .document(uid)
                .addSnapshotListener { documentSnapshot, e ->
                    if (e != null) {
                        task.complete(Result.failure(e))
                        return@addSnapshotListener
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val lessonAccount = documentSnapshot.toObject(LessonAccount::class.java)
                        if (lessonAccount != null) {
                            task.complete(Result.success(lessonAccount))
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
                                    task.complete(Result.success(newLessonAccount))
                                }
                                .addOnFailureListener { exception ->
                                    task.complete(Result.failure(exception))
                                }
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
                                task.complete(Result.success(newLessonAccount))
                            }
                            .addOnFailureListener { exception ->
                                task.complete(Result.failure(exception))
                            }
                    }
                }
            task.await()
        } catch (e: Exception) {
            Result.failure(e)
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