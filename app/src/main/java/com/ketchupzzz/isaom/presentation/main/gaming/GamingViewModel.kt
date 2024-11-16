package com.ketchupzzz.isaom.presentation.main.gaming

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.games.GameSubmission
import com.ketchupzzz.isaom.models.games.Games
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.repository.game.GameRepository
import com.ketchupzzz.isaom.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@HiltViewModel
class GamingViewModel @Inject constructor(
    private  val gameRepository: GameRepository,
) : ViewModel() {
    var state by mutableStateOf(GamingState())

    fun events(e :GamingEvents) {
        when(e) {
            is GamingEvents.OnGetLevels -> getLevels(e.game)
            is GamingEvents.OnSetUsers -> state = state.copy(
                users = e.users
            )
            is GamingEvents.OnStartTimer -> startTimer(e.time)
            is GamingEvents.OnAnswerChanged -> addAnswer(e.text,e.index)
            GamingEvents.OnReset -> reset()
            is GamingEvents.OnAddAnser -> saveAnswer(e.answers)
            is GamingEvents.OnFinish -> submitGame(e.gameSubmission,e.navHostController)
            is GamingEvents.OnIncrementScore -> {
                val currentScore = state.score + e.points
                state = state.copy(score = currentScore)
            }
        }
    }

    private fun submitGame(gameSubmission: GameSubmission,navHostController: NavHostController) {
        viewModelScope.launch {
            gameRepository.submitScore(gameSubmission) {
              when(it) {
                    is UiState.Error ->   state = state.copy(
                        isSubmitting = false,
                        errors = it.message
                    )
                    UiState.Loading ->   state = state.copy(
                        isSubmitting = true,
                        errors = null,
                    )
                    is UiState.Success -> {
                        state = state.copy(
                            isSubmitting = false,
                            errors = null,
                            submitted = it.data
                        )
                        navHostController.popBackStack()
                    }
                }
            }
        }
    }

    private fun saveAnswer(answers: Pair<String, String>) {
        state = state.copy(
            answerSheet = state.answerSheet + answers
        )
    }

        private fun reset() {
            state = state.copy(
                answers = "",
                answerIndex = emptyList()
            )
        }

    private fun addAnswer(text: String, index: Int) {
        val current = state.answerIndex.toMutableList()
        current.add(index)
        state = state.copy(
            answers = state.answers + text,
            answerIndex = current
        )
    }

    private var countDownTimer: CountDownTimer? = null

    private fun startTimer(time: Int) {
        countDownTimer?.cancel() // Cancel any previous timer to avoid multiple timers running
        val timeInMillis = time * 60 * 1000
        countDownTimer = object : CountDownTimer(timeInMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                state = state.copy(timer = (millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                state = state.copy(timer = 0, timerFinish = true)
                println("Timer finished")
            }
        }
        countDownTimer?.start()
    }





    private fun getLevels(games: Games) {
        viewModelScope.launch {

            gameRepository.getAllLevels(games.id!!) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> {

                        state.copy(
                            isLoading = false,
                            errors = null,
                            levels = it.data,
                            timer = 0
                        )
                    }
                }
                events(GamingEvents.OnStartTimer(games.timer))
            }
        }
    }
}