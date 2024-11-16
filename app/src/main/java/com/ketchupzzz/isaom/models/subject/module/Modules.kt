package com.ketchupzzz.isaom.models.subject.module

import android.text.Html
import java.util.Date



data class Modules(
    val id : String ? = null,
    val subjectID : String? = null,
    val title : String ? = null,
    val desc : String ? = null,
    var content : String ? = null,
    val open : Boolean = false,
    val createdAt : Date  = Date(),
)

