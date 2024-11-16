package com.ketchupzzz.isaom.presentation.main.dictionary

import androidx.paging.PagingData
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.dictionary.remote.Dictionary

import com.ketchupzzz.isaom.models.dictionary.remote.Favorites
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


data class DictionaryState(
    val isLoading : Boolean = false,
    val words: Flow<PagingData<Dictionary>> = emptyFlow(),
    val dictionaryList: Flow<PagingData<Dictionary>> = emptyFlow(),
    val favorites  : List<Favorites> = emptyList(),
    val users : Users ? = null,
    val errors : String ? = null,
    val message : String ? = null,
    val isGettingFavorites: Boolean = false,
    val filter : String = "",
)