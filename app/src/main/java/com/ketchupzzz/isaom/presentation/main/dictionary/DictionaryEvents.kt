package com.ketchupzzz.isaom.presentation.main.dictionary

import android.content.Context
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.dictionary.remote.Dictionary

sealed interface DictionaryEvents {
    data object OnGetAllDictionaries : DictionaryEvents
    data class OnAddToFavorites(val dictionary: Dictionary) : DictionaryEvents
    data class OnGetDictionary(val uid : String) : DictionaryEvents
    data class RemoveToFavorites(val id : String,val context : Context) : DictionaryEvents
    data class OnGetUsers(val users : Users ?) : DictionaryEvents
    data class OnSearching(val text : String) : DictionaryEvents
}