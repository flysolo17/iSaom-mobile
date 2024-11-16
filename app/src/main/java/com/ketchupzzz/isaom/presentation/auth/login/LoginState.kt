package com.ketchupzzz.isaom.presentation.auth.login

import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.utils.Email
import com.ketchupzzz.isaom.utils.Password
import com.ketchupzzz.isaom.utils.StudentID

data class LoginState(
    val email : Email = Email(),
    val password : Password = Password(),
    val isPasswordVisible : Boolean = false,
    val isLoading : Boolean = false,
    val error : String? = null,
    val isLoggedIn : Boolean = false,
    val users : Users? = null,
)