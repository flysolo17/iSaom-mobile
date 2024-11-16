package com.ketchupzzz.isaom.presentation.auth.register

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.models.sections.Sections
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.repository.sections.SECTION_COLLECTION
import com.ketchupzzz.isaom.repository.sections.SectionRepository
import com.ketchupzzz.isaom.utils.hasNumbers
import com.ketchupzzz.isaom.utils.hasSpaces
import com.ketchupzzz.isaom.utils.isLessThanSix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class RegisterViewModel @Inject constructor(
    private  val sectionRepository: SectionRepository,
    private val authRepository : AuthRepository
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
    init {
        getAllGenders()
        getAllSections()

    }

    private fun getAllGenders() {
        viewModelScope.launch {
            authRepository.getAllGenderSelection {
                if (it is UiState.Success) {
                    state = state.copy(
                        genderSelection = it.data
                    )
                }
            }
        }

    }

    fun onEvent(registerEvents: RegisterEvents) {
        when(registerEvents) {
            is RegisterEvents.OnEmailChange -> emailChanged(registerEvents.email)
            is RegisterEvents.OnNameChange -> nameChanged(registerEvents.name)
            is RegisterEvents.OnPasswordChange -> passwordChanged(registerEvents.password)
            is  RegisterEvents.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordHidden = !state.isPasswordHidden
                )
            }
            is RegisterEvents.OnSectionChange -> sectionChanged(registerEvents.section)
            RegisterEvents.OnCreateAccount -> createAccount()
            is RegisterEvents.OnSelectGender -> selectGender(registerEvents.gender,registerEvents.url)
            is RegisterEvents.OnUserTypeSelected -> {
                state = state.copy(
                    userType = registerEvents.type
                )
            }
            is RegisterEvents.OnConfirmPasswordChange -> confirmPasswordChange(
                registerEvents.password
            )

            RegisterEvents.OnToggleConfirmPasswordVisibility ->{
                state = state.copy(
                    isConfirmPasswordHidden = !state.isConfirmPasswordHidden
                )
            }
        }
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


    private fun confirmPasswordChange(confirmPassword: String) {
        val originalPassword = state.password.value
        val hasError = confirmPassword != originalPassword
        val errorMessage = if (hasError) "Passwords do not match" else null

        val currentConfirmPassword = state.confirmPassword.copy(
            value = confirmPassword,
            isError = hasError,
            errorMessage = errorMessage
        )
        state = state.copy(confirmPassword = currentConfirmPassword)
    }

    private fun selectGender(gender: Gender?, url: String?) {
        state = state.copy(
            selectedGender = gender,
            selectedGenderUrl = url
        )
    }

    private fun createAccount() {
        val name : String = state.name.value
        val email : String = state.email.value
        val password : String = state.password.value

        val gender : Gender  = state.selectedGender?: Gender.MALE
        val section : String = state.section?.id ?: ""
        if (section == "" && state.userType === UserType.STUDENT) {
            return
        }
        authRepository.register(
            name =name,
            email = email,
            password = password,
            gender = gender,
            type = state.userType ?: UserType.GUEST,
            sectionID = section,
            avatar = state.selectedGenderUrl ?:""
        ) {
            when(it) {
                is UiState.Error -> {
                    state = state.copy(
                        isLoading = false,
                        errors =  it.message
                    )

                }

                is UiState.Loading -> {
                    state = state.copy(
                        isLoading = true,
                        errors = null,
                    )
                }
                is UiState.Success -> {
                    authRepository.setUser(it.data)
                    state = state.copy(
                        isLoading = false,
                        registerSuccess = true,
                        errors = null
                    )
                }
            }
        }
    }

    private fun sectionChanged(sections: Sections?) {
        state = state.copy(
            section = sections,
        )
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



    private fun getAllSections() {
        sectionRepository.getAllSections {
            when(it) {
                is UiState.Error ->  {
                    Log.e(SECTION_COLLECTION,it.message)
                }
                UiState.Loading -> {
                    Log.d(SECTION_COLLECTION,"Loading..")
                }
                is UiState.Success -> {
                    Log.d(SECTION_COLLECTION,it.data.toString())
                    state = state.copy(
                        sectionList = it.data
                    )
                }
            }
        }
    }
}