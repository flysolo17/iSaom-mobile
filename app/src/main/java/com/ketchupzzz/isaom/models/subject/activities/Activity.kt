package com.ketchupzzz.isaom.models.subject.activities

import com.ketchupzzz.isaom.utils.generateRandomString
import java.util.Date

data class Activity(
    val id : String ? = generateRandomString(),
    val subjectID : String ? = null,
    val title : String ? = null,
    val desc : String ? = null,
    val open : Boolean  =false,
    val createdAt : Date ? = Date()
)
