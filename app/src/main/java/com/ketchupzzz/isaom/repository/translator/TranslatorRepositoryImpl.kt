package com.ketchupzzz.isaom.repository.translator

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.ketchupzzz.isaom.models.history.TranslatorHistory
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.services.TranslationResponse
import com.ketchupzzz.isaom.services.TranslatorService
import kotlinx.coroutines.tasks.await
import retrofit2.Response

class TranslatorRepositoryImpl(
    private  val translatorService: TranslatorService,
    private val firestore: FirebaseFirestore
): TranslatorRepository {

    override fun translateText(
        text: String,
        source : String,
        target : String,
        result: (UiState<TranslationResponse>) -> Unit
    ) {
        // Indicate that the request is in progress
        result(UiState.Loading)

        // Make the API call
        val call = translatorService.translateText(text,source,target)
        call.enqueue(object : retrofit2.Callback<TranslationResponse> {
            override fun onResponse(
                call: retrofit2.Call<TranslationResponse>,
                response: Response<TranslationResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        result(UiState.Success(responseBody))
                    } else {
                        result(UiState.Error(response.errorBody().toString()))
                    }
                } else {

                    val errorMessage = "HTTP error: ${response.code()} ${response.message()}"
                    result(UiState.Error(errorMessage))
                }
            }

            override fun onFailure(call: retrofit2.Call<TranslationResponse>, t: Throwable) {
                result(UiState.Error("Network error: ${t.message}"))
            }
        })
    }

    override fun translateImage(context: Context, uri: Uri, source: String, target: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTranslator(
        translatorHistory: TranslatorHistory,
    ) {
        try {
            if (translatorHistory.userID.isNullOrEmpty()) {
                return
            }
            val currentData = firestore
                .collection(TRANSLATION_COLLECTION)
                .whereEqualTo("text",translatorHistory.text)
                .get()
                .await()
                .toObjects<TranslatorHistory>()
            if (currentData.any { it.text?.trim()?.lowercase() == translatorHistory.text?.trim()?.lowercase() }) {
                return
            }

            firestore.collection(TRANSLATION_COLLECTION)
                .document(translatorHistory.id ?: "")
                .set(translatorHistory)
                .await()

        } catch (e : Exception) {
            Log.e(TRANSLATION_COLLECTION,"error",e)

        }
    }

    override suspend fun getAllTranslationHistory(limit: Int ? ,result: (UiState<List<TranslatorHistory>>) -> Unit) {

        val query = if (limit != null)
            firestore.collection(TRANSLATION_COLLECTION)
                .orderBy("createdAt",Query.Direction.DESCENDING)
                .limit(limit.toLong())
        else firestore.collection(TRANSLATION_COLLECTION)
            .orderBy("createdAt",Query.Direction.DESCENDING)

        query.addSnapshotListener { value, error ->
                error?.let {

                    result.invoke(UiState.Error(it.localizedMessage ?: "Unknown error"))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(TranslatorHistory::class.java)))
                }
            }
    }

    companion object {
        const val TRANSLATION_COLLECTION = "translations"
    }
}