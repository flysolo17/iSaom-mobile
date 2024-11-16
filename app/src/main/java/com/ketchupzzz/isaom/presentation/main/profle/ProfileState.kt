package com.ketchupzzz.isaom.presentation.main.profle

import com.ketchupzzz.isaom.models.Users


data class ProfileState(
    val isLoading : Boolean = false,
    val users: Users ? = null,
    val errors : String ? = null,
    val isLoggedOut : Boolean = false
)