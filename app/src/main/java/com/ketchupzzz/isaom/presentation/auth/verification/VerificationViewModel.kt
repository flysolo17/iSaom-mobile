package com.ketchupzzz.isaom.presentation.auth.verification

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class VerificationViewModel @Inject constructor(
     private val authRepository : AuthRepository
) : ViewModel() {
    var state by mutableStateOf(VerificationState())
    private var timerJob: Job? = null
    init {
        events(VerificationEvents.OnListenToUserVerification)
        events(VerificationEvents.OnGetCurrentUser)
    }

    fun events(e : VerificationEvents) {
        when(e) {
            VerificationEvents.OnListenToUserVerification -> listenToUserVerification()
            is VerificationEvents.OnSendVerification -> sendVerificationEmail(e.context)
            VerificationEvents.OnGetCurrentUser -> getUsers()
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            authRepository.getCurrentUser {
                if (it is UiState.Success) {
                    state = state.copy(users = it.data , isVerified = it.data?.verified ?: false)
                }
            }
        }
    }

    private fun sendVerificationEmail(context : Context) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            authRepository.sendEmailVerification { uiState ->
                state = when (uiState) {
                    is UiState.Success -> {
                        startTimer()
                        context.toast(uiState.data)
                        state.copy(
                            isLoading = false,
                            errors = null,
                        )
                    }

                    is UiState.Error -> {
                        context.toast(
                            uiState.message
                        )
                        state.copy(isLoading = false, errors = null)
                    }

                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel() // Cancel any existing timer job
        timerJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                delay(1000L) // Wait for 1 second
                state = state.copy(timer = i)
            }
        }
    }

    private fun listenToUserVerification() {
        viewModelScope.launch {
            while (true) {
                delay(3000)
            authRepository.listenToUserEmailVerification { uiState ->
                    if (uiState is UiState.Success) {
                        state = state.copy(isVerified = uiState.data)
                    }
                }
            }
        }
    }
}