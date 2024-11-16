package com.ketchupzzz.isaom.repository.lessons

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.SignLanguageLesson

const val SIGN_LANGUAGE_LESSONS = "sign-language-lessons"
class LessonRepositoryImpl(private  val firestore: FirebaseFirestore): LessonRepository {
    override fun getAllLessons(result: (UiState<List<SignLanguageLesson>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(SIGN_LANGUAGE_LESSONS)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(SignLanguageLesson::class.java)))
                }
            }
    }
}