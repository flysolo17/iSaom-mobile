package com.ketchupzzz.isaom.models.history

import com.ketchupzzz.isaom.models.SourceAndTargets
import com.ketchupzzz.isaom.utils.generateRandomString
import java.util.Date


data class TranslatorHistory(
    val id : String ? = generateRandomString(),
    val userID : String ? = null,
    val text : String ? = null,
    val translation : String ? = null,
    val source : SourceAndTargets ?  = SourceAndTargets.ENGLISH,
    val target : SourceAndTargets  ? = SourceAndTargets.ILOCANO,
    val createdAt : Date ? = Date()
)