package com.ketchupzzz.isaom.presentation.auth.login

sealed interface LoginEvents {
    data class OnEmailChanged(val email: String) : LoginEvents
    data class OnPasswordChanged(val password: String) : LoginEvents
    data object OnTogglePasswordVisibility : LoginEvents
    data object OnLogin : LoginEvents
    data object OnGetCurrentUser : LoginEvents
}