package com.ketchupzzz.isaom.models.games


import java.util.Date

data class Games(
    val id : String  ? = null,
    val title : String ? = "",
    val timer : Int = 0,
    val cover : String ? = null,
    val createdAt : Date = Date()
)
