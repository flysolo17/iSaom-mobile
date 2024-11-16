package com.ketchupzzz.isaom.presentation.auth.register

import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.models.sections.Sections
import com.ketchupzzz.isaom.models.UserType

sealed interface RegisterEvents {
     data class   OnNameChange(val name : String) : RegisterEvents
    data class   OnEmailChange(val email : String) : RegisterEvents

    data class OnUserTypeSelected(val type: UserType) : RegisterEvents
    data class   OnPasswordChange(val password : String) : RegisterEvents
    data class   OnConfirmPasswordChange(val password : String) : RegisterEvents
    data class   OnSectionChange(val section : Sections?) : RegisterEvents
    data object OnTogglePasswordVisibility : RegisterEvents
    data object OnToggleConfirmPasswordVisibility : RegisterEvents
    data object OnCreateAccount : RegisterEvents
    data class OnSelectGender(val gender: Gender? ,val url : String?) : RegisterEvents
}