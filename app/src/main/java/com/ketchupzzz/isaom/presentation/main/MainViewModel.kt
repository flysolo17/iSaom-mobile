package com.ketchupzzz.isaom.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {


    var state by mutableStateOf(MainState())
    init {
        events(MainEvents.OnGetUser)
    }
    fun events(e : MainEvents) {
        when(e) {
            MainEvents.OnGetUser -> getUser()
        }
    }

    private fun getUser() {
        state = state.copy(
            users = authRepository.getUsers()
        )
    }
}