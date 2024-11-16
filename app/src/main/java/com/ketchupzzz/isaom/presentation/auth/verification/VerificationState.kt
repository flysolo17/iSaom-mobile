package com.ketchupzzz.isaom.presentation.auth.verification

import com.ketchupzzz.isaom.models.Users

data class VerificationState(
    val isLoading : Boolean = false,
    val timer : Int = 0,
    val errors : String ? = null,
    val isVerified: Boolean = false,
    val users: Users ? = null,
)