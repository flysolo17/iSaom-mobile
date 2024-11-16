package com.ketchupzzz.isaom.presentation.auth.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.utils.hasSpaces
import com.ketchupzzz.isaom.utils.isLessThanSix
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())

    init {
        onEvent(LoginEvents.OnGetCurrentUser)
    }
    fun onEvent(events: LoginEvents) {
        when(events) {
            is LoginEvents.OnLogin -> login()
            is LoginEvents.OnPasswordChanged -> passwordChanged(events.password)
            is LoginEvents.OnEmailChanged -> emailChanged(events.email)
            is LoginEvents.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            LoginEvents.OnGetCurrentUser -> getCurrentUser()
        }
    }

    private fun getCurrentUser() {
        authRepository.getCurrentUser  {
            state = when(it) {
                is UiState.Error -> {
                    state.copy(isLoading = false, error = it.message)
                }
                is UiState.Loading -> {
                    state.copy(isLoading = true, error = null)
                }
                is UiState.Success -> {
                    authRepository.setUser(it.data)
                    state.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        error = null,
                        users = it.data
                    )
                }
            }
        }
        state = state.copy(error = null)
    }

    private fun login() {
        val id = state.email
        val password = state.password
        if (id.value.isEmpty() || password.value.isEmpty()) {
            state = state.copy(
                email = id.copy(
                    isError = true,
                    errorMessage = "Email cannot be empty"
                ),
                password = password.copy(
                    isError = true,
                    errorMessage = "Password cannot be empty"
                )
            )
            return
        }

        authRepository.login(email = id.value, password = password.value) {
            state = when(it) {
                is UiState.Error -> {
                    state.copy(isLoading = false, error = it.message)
                }
                is UiState.Loading -> {
                    state.copy(isLoading = true, error = null)
                }
                is UiState.Success -> {
                    authRepository.setUser(it.data)
                    state.copy(isLoading = false, isLoggedIn = true, error = null, users = it.data)
                }
            }
        }

        state = state.copy(error = null)
    }

    private fun emailChanged(email : String) {
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

    private fun passwordChanged(password : String) {

        val hasError = password.isLessThanSix() || password.hasSpaces()
        val errorMessage = if (password.isLessThanSix()) {
            "Password must be at least 6 characters"
        } else if (password.hasSpaces()) {
            "Password cannot contain spaces"
        } else {
            null
        }
        val currentPassword = state.password.copy(
            value = password,
            isError = hasError,
            errorMessage = errorMessage
        )
        state = state.copy(
            password =  currentPassword
        )
    }
}