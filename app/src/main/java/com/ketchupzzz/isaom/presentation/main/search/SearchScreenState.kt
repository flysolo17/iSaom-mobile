package com.ketchupzzz.isaom.presentation.main.search

import com.ketchupzzz.isaom.models.dictionary.remote.Dictionary

data class SearchScreenState(
    val isLoading : Boolean = false,
    val words : List<Dictionary> = emptyList(),
    val error : String? = null,
    val filter : String = "",
)