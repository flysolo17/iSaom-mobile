package com.ketchupzzz.isaom.models.dictionary.remote

import com.ketchupzzz.isaom.utils.generateRandomString

data class Favorites(
    val id : String = generateRandomString(10),
    val userID :String ? = null,
    val dictionary: Dictionary? = null,
)
