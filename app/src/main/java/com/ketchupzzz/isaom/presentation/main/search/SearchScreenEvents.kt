package com.ketchupzzz.isaom.presentation.main.search

import android.app.appsearch.AppSearchManager.SearchContext


sealed interface SearchScreenEvents {
    data class OnSearch(val word : String) : SearchScreenEvents
    data class OnFilterChanged(val filter : String) : SearchScreenEvents
}