package com.ketchupzzz.isaom.models.games

import java.util.Date

data class Levels(
    val id : String ? = null,
    val question : String ? = null,
    val image : String  ? = null,
    val hint : String ? = null,
    val points : Int = 0,
    val answer : String ? = null,
    val createdAt : Date = Date()
)

fun List<Levels>.geMaxScore() : Int {
    return this.sumOf { it.points }
}
fun List<Levels>.getScore(answerSheet: Map<String, String>): Int {
    return answerSheet.entries.sumOf { (levelID, answer) ->
        val level = this.find { it.id == levelID }
        if (level != null && level.answer.equals(answer, ignoreCase = true)) {
            level.points
        } else {
            0
        }
    }
}
