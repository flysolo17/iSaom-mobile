package com.ketchupzzz.isaom.models.history

import android.content.Context
import androidx.annotation.DrawableRes
import com.ketchupzzz.isaom.R

data class History(
    val title : String,
    @DrawableRes val image : Int ? = null,
    val desc : String
) {



    companion object {

    }
}

fun Context.initHistories()  : List<History> {
    val HISTORIES : List<History> = listOf(
        History(
            title = "Culture",
            image = R.drawable.culture,
            desc = this.getString(R.string.culture)
        ),
        History(
            title = "Traditions",
            image = R.drawable.traditions,
            desc = this.getString(R.string.traditions)
        ),
        History(
            title = "Foods",
            image = R.drawable.food,
            desc = this.getString(R.string.foods)
        ),

        History(
            title = "Sports",
            desc = this.getString(R.string.sports)
        ),
        History(
            title = "Clothing",
            image= R.drawable.clothing,
            desc = this.getString(R.string.clothing)
        ),
    )
    return HISTORIES;
}
