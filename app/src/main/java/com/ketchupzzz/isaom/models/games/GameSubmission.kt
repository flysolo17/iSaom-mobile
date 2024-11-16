package com.ketchupzzz.isaom.models.games

import java.util.Date


data class GameSubmission(
    val id : String ? = null,
    val gameID : String? = null,
    val userID : String ? = null,
    val score : Int  = 0,
    val maxScore : Int = 0,
    val answerSheet : Map<String,String> = emptyMap(),
    val createdAt : Date = Date()
)


fun List<GameSubmission>.getMyHighestScorePerGameID(): List<GameSubmission> {
    return this
        .filter { it.gameID != null }
        .groupBy { it.gameID }
        .map { (_, submissions) ->
            submissions.maxByOrNull { it.score }!!
        }
}