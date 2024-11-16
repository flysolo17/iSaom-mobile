package com.ketchupzzz.isaom.presentation.auth.forgotPassword

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.utils.hasSpaces
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel

class ForgotPasswordViewModel @Inject constructor(
     private val repository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ForgotPasswordState())


    fun events(events: ForgotPasswordEvents) {
        when(events) {
            is ForgotPasswordEvents.OnResetPassword -> resetPassword(events.email)
            is ForgotPasswordEvents.OnEmailChaged -> emailChanged(events.email)
        }
    }

    private fun emailChanged(email: String) {
        val hasError =  !Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.hasSpaces()
        val errorMessage = if (hasError)  {
            "Invalid email"
        } else {
            null
        }
        val newEmail = state.email.copy(
            value = email,
            isError = hasError,
            errorMessage = errorMessage
        )
        state = state.copy(
            email = newEmail,
        )
    }

    private fun resetPassword(email: String) {
        repository.forgotPassword(email) {
            state = when(it) {
                is UiState.Error -> state.copy(isLoading = false, errors = it.message)
                is UiState.Loading -> state.copy(isLoading = true, errors = null)
                is UiState.Success -> state.copy(isLoading = false, errors = null, isSent = it.data)
            }
        }
    }
}