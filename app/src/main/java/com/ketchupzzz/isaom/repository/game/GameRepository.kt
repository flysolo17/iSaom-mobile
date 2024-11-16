package com.ketchupzzz.isaom.repository.game
import com.ketchupzzz.isaom.models.games.Games
import com.ketchupzzz.isaom.models.WordTranslate
import com.ketchupzzz.isaom.models.games.GameSubmission
import com.ketchupzzz.isaom.models.games.Levels
import com.ketchupzzz.isaom.models.games.UserWithGameSubmissions

import com.ketchupzzz.isaom.utils.UiState

interface GameRepository {
    suspend fun getAllGames(result : (UiState<List<Games>>) -> Unit)

    suspend fun getAllLevels(gameID : String,result: (UiState<List<Levels>>) -> Unit)

    suspend fun submitScore(
        gameSubmission: GameSubmission,
        result : (UiState<String>) -> Unit
    )
    suspend fun getScores(
        result: (UiState<List<UserWithGameSubmissions>>) -> Unit
    )
}