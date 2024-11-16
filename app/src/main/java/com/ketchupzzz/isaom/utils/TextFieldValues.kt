package com.ketchupzzz.isaom.utils

data class Fullname(
    val value : String = "",
    val isError : Boolean = false,
    val errorMessage : String? = null
)

data class Email(
    val value : String = "",
    val isError : Boolean = false,
    val errorMessage : String? = null
)

data class StudentID(
    val value : String = "",
    val isError : Boolean = false,
    val errorMessage : String? = null
)

data class Password(
    val value : String = "",
    val isError : Boolean = false,
    val errorMessage : String? = null
)





fun String.isLessThanSix()  : Boolean {
    return this.length < 6
}
fun String.hasSpaces() : Boolean {
    return this.contains(" ")
}

fun String.noUpperCase() : Boolean {
    return !this.any { it.isUpperCase() }
}
fun String.hasNumbers() : Boolean {
    return this.any { it.isDigit() }
}

fun String.hasSpecialCharacters() : Boolean {
    return this.any { !it.isLetter() }
}

fun Int.toLetter(): String {
    var num = this
    val result = StringBuilder()

    while (num >= 0) {
        result.append('A' + (num % 26))
        num = (num / 26) - 1
    }

    return result.reverse().toString()
}
