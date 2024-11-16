package com.ketchupzzz.isaom.models.subject.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
data class Content(
    val id  : String ? = null,
    val title : String ? = null,
    val desc : String ? = null,
    val body : String ? = null,
    var image : String ? = null,
    val createdAt : Date = Date()
) : Parcelable