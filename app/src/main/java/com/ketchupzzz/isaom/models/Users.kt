package com.ketchupzzz.isaom.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Users(
    val id : String ? = null,
    val name : String ? = null,
    val email : String ? = null,
    val type : UserType ? = null,
    val sections : List<String>  = emptyList(),
    val gender : Gender ? = null,
    val avatar : String ? = null,
    val verified : Boolean ? = null,
) : Parcelable




enum class UserType {
    STUDENT,
    TEACHER,
    GUEST
}

enum class Gender {
    MALE,
    FEMALE ,
}