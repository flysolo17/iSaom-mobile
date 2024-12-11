package com.ketchupzzz.isaom.presentation.main.profle

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class ProfileViewModel @Inject constructor(
     private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
    init {
        events(ProfileEvents.GeUserInfg)
    }

    fun events(profileEvents: ProfileEvents) {
        when(profileEvents) {
            ProfileEvents.OnLoggedOut -> logout()
            ProfileEvents.GeUserInfg -> getInfo()
            is ProfileEvents.SelectProfile -> updateProfile(profileEvents.uri)
        }
    }

    private fun updateProfile(uri: Uri) {
        viewModelScope.launch {
            state.users?.id?.let {
                authRepository.changeProfile(it,uri) {
                    state = when(it) {
                        is UiState.Error -> state.copy(
                            errors = it.message,
                            isLoading = false
                        )
                        UiState.Loading -> state.copy(
                            isLoading = true,
                            errors = null
                        )
                        is UiState.Success -> state.copy(
                            isLoading = false,
                            errors = null,
                            messages = it.data
                        )
                    }
                }
            }
            delay(1000)
            state = state.copy(
                messages = null
            )
        }
    }

    private fun getInfo() {

        viewModelScope.launch {
            authRepository.getCurrentUser {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        users = it.data
                    )
                }
            }
        }
    }

    private fun logout() {
        authRepository.logout {
            when(it) {
                is UiState.Error -> {
                    state = state.copy(isLoading = false, errors = it.message)
                }
                UiState.Loading -> {   state = state.copy(isLoading = true, errors = null)}
                is UiState.Success -> {
                    authRepository.setUser(null)
                    state = state.copy(isLoading = false, isLoggedOut = true)

                }
            }
        }
    }

}