package com.ketchupzzz.isaom.presentation.auth.forgotPassword

import com.ketchupzzz.isaom.utils.Email

data class ForgotPasswordState(
    val isLoading : Boolean = false,
    val isSent : String ? = null,
    val errors : String ? = null,
    val email : Email = Email()
)