package com.ketchupzzz.isaom.repository.auth

import android.net.Uri
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.models.GenderSelection
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.models.Users

interface AuthRepository {


    fun getUsers() : Users?
    fun getCurrentUser(result: (UiState<Users?>) -> Unit)
    fun setUser(users: Users ?)
    fun login(email : String, password : String ,result : (UiState<Users>) -> Unit)

    fun register(
        name : String,
        sectionID : String,
        type : UserType,
        gender: Gender,
        email : String,
        password: String,
        avatar : String,
        result : (UiState<Users>) -> Unit
    )

    fun logout(
        result: (UiState<String>) -> Unit
    )




    suspend fun getAllGenderSelection(result: (UiState<GenderSelection>) -> Unit)

    fun forgotPassword(email : String,result: (UiState<String>) -> Unit)

    suspend fun changePassword(oldPassword : String,newPassword : String,result: (UiState<String>) -> Unit)
    suspend fun updateUserInfo(
         uid : String,
         name : String,
         result: (UiState<String>) -> Unit
    )

    suspend fun getStudentsInSubject(
        studentIDS: List<String>,
        result: (UiState<List<Users>>) -> Unit
    )

    suspend fun sendEmailVerification(result: (UiState<String>) -> Unit)

    suspend fun listenToUserEmailVerification(result: (UiState<Boolean>) -> Unit)

}