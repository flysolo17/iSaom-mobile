package com.ketchupzzz.isaom.repository.dictionary

import androidx.paging.PagingData
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.dictionary.remote.Dictionary
import com.ketchupzzz.isaom.models.dictionary.remote.Favorites
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    suspend fun getAllDictionaries(result : (UiState<List<Dictionary>>) -> Unit)
    suspend fun addToFavorites(favorites: Favorites, result: (UiState<String>) -> Unit)
    suspend fun getAllFavorites(userID : String,result: (UiState<List<Favorites>>) -> Unit)
    suspend fun removeToFavorites(id : String,result: (UiState<String>) -> Unit)
    fun getDictionaryEntries(): Flow<PagingData<Dictionary>>

    suspend fun searchWord(word : String,result: (UiState<List<Dictionary>>) -> Unit)
}