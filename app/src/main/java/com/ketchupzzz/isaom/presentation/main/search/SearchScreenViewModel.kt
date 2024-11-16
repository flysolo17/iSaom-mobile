package com.ketchupzzz.isaom.presentation.main.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.models.dictionary.remote.Dictionary
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.repository.dictionary.DictionaryRepository
import com.ketchupzzz.isaom.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {
    var state by mutableStateOf(SearchScreenState())
    fun events(e : SearchScreenEvents) {
        when(e) {
            is SearchScreenEvents.OnSearch -> searc(e.word)
            is SearchScreenEvents.OnFilterChanged -> state = state.copy(
                filter = e.filter
            )
        }
    }

    private fun searc(word: String) {
        viewModelScope.launch {
            dictionaryRepository.searchWord(word){
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        words = emptyList(),
                        error = it.message,
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        words = emptyList(),
                        error = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        error = null,
                        words = it.data
                    )
                }
            }
        }
    }


}