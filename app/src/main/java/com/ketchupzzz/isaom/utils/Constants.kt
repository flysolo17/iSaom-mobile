package com.ketchupzzz.isaom.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

fun generateRandomString(length: Int = 15): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var result = ""

    for (i in 0 until length) {
        val randomIndex = (Math.random() * characters.length).toInt()
        result += characters[randomIndex]
    }

    return result
}


fun Int.indexToLetter(): Char {
    require(this >= 0) { "Index must be non-negative" }
    return ('A' + this)
}


fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )

    return image
}

fun Context.toast(
    message : String
) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun generateRandomColors(size: Int): List<Color> {
    val predefinedColors = listOf(
        Color(0xFFE57373),
        Color(0xFF81C784),
        Color(0xFF64B5F6),
        Color(0xFFFFD54F),
        Color(0xFFBA68C8),
        Color(0xFF4DB6AC),
        Color(0xFFFF8A65),
        Color(0xFF9575CD),
        Color(0xFFAED581),
        Color(0xFFFFB74D)
    )

    return List(size) { index ->
        if (index < predefinedColors.size) {
            predefinedColors[index]
        } else {
            Color(0xFFFF0000)
        }
    }
}



fun String.shuffleString(): String {
    val characters = this.replace(" ","")
        .toList()
    val shuffledCharacters = characters.shuffled()
    return shuffledCharacters.joinToString("")
}

fun String.getSpacesInString(): List<Int> {
    return this.mapIndexedNotNull { index, char ->
        if (char == ' ') index else null
    }
}
