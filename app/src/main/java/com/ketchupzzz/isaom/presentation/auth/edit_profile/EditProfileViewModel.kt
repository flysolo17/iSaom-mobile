package com.ketchupzzz.isaom.presentation.auth.edit_profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.utils.hasNumbers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class EditProfileViewModel @Inject constructor(
     private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(EditProfileState())
    fun events(e : EditProfileEvents) {
        when(e) {


            is EditProfileEvents.OnNameChange -> nameChanged(e.name)

            is EditProfileEvents.OnSaveChanges -> saveChanges(
                e.uid,
                e.name,


            )
        }
    }

    private fun nameChanged(name : String) {
        val hasError = name.hasNumbers()
        val errorMessage = if (hasError)  {
            "Invalid name"
        } else {
            null
        }
        state =  state.copy(
            name =state.name.copy(
                value = name,
                isError = hasError,
                errorMessage = errorMessage
            ),
        )
    }

    private fun saveChanges(uid: String, name: String) {
        viewModelScope.launch {
            authRepository.updateUserInfo(uid,name) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isSaving = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isSaving = true,
                        errors = null,
                    )
                    is UiState.Success -> state.copy(
                        isSaving = false,
                        errors = null,
                        isDoneSaving = it.data
                    )
                }
            }
        }
    }
}