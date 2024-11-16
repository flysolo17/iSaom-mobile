package com.ketchupzzz.isaom.repository.game

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.firestore.toObjects
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.games.Games
import com.ketchupzzz.isaom.models.WordTranslate
import com.ketchupzzz.isaom.models.games.GameSubmission
import com.ketchupzzz.isaom.models.games.Levels
import com.ketchupzzz.isaom.models.games.UserWithGameSubmissions
import com.ketchupzzz.isaom.models.games.getMyHighestScorePerGameID

import com.ketchupzzz.isaom.repository.auth.USERS_COLLECTION

import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.utils.generateRandomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.http.Query
import java.io.IOException

class GameRepositoryImpl(
    val context  : Context,
    val auth : FirebaseAuth,
    val firestore : FirebaseFirestore
): GameRepository {


    override suspend fun getAllGames(result: (UiState<List<Games>>) -> Unit) {
        result(UiState.Loading)
        firestore.collection(GAME_COLLECTION)
            .addSnapshotListener { value, error ->
                value?.let {
                    result(UiState.Success(it.toObjects(Games::class.java)))
                }
                error?.let {
                    result(UiState.Error(it.message.toString()))
                }
            }
    }

    override suspend fun getAllLevels(gameID: String, result: (UiState<List<Levels>>) -> Unit) {
        result.invoke(UiState.Loading)
        delay(1000)
        firestore.collection(GAME_COLLECTION)
            .document(gameID)
            .collection(LEVELS_COLLECTION)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result.toObjects(Levels::class.java).shuffled()
                    result.invoke(UiState.Success(data))
                } else {
                    result.invoke(UiState.Error("Something wrong fetching levels"))
                }
            }.addOnFailureListener {
                result(UiState.Error(it.message.toString()))
            }
    }

    override suspend fun submitScore(
        gameSubmission: GameSubmission,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(GAME_SUBMISSIONS)
            .document(gameSubmission.id!!)
            .set(gameSubmission)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Submitted"))
                } else {
                    result.invoke(UiState.Error("Something wrong levels"))
                }
            }.addOnFailureListener {
                result(UiState.Error(it.message.toString()))
            }
    }

    override suspend fun getScores(result: (UiState<List<UserWithGameSubmissions>>) -> Unit) {
        result.invoke(UiState.Loading)
        try {
            val submissions = firestore.collection(GAME_SUBMISSIONS)
                .get()
                .await()
                .toObjects(GameSubmission::class.java)
            val users = firestore
                .collection(USERS_COLLECTION)
                .get()
                .await()
                .toObjects(Users::class.java)

            val userWithHighestScores = submissions
                .groupBy { it.userID }
                .mapNotNull { (userID, userSubmissions) ->
                    userID?.let { id ->
                        val user = users.first { it.id == id }
                        val highestScoresPerGame = userSubmissions.getMyHighestScorePerGameID()
                        val totalScore = highestScoresPerGame.sumOf { it.score }
                        UserWithGameSubmissions(user = user, submission =  highestScoresPerGame , totalScore = totalScore)
                    }
                }
            result(UiState.Success(userWithHighestScores))
        } catch (e : Exception) {
            result(UiState.Error(e.message.toString()))
        }
    }


    companion object {
        const val GAME_COLLECTION = "games"
        const val LEVELS_COLLECTION = "levels"
        const val GAME_SUBMISSIONS = "matches"
    }


}