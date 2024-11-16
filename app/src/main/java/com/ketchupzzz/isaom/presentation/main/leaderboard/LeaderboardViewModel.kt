package com.ketchupzzz.isaom.presentation.main.leaderboard

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.repository.game.GameRepository
import com.ketchupzzz.isaom.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    var state by mutableStateOf(LeaderboardState())

    init {
        events(LeaderboardEvents.OnGetLeaderboard)
    }
    fun events(e : LeaderboardEvents) {
        when(e) {
            LeaderboardEvents.OnGetLeaderboard -> getLeaderboard()
        }
    }

    private fun getLeaderboard() {
        viewModelScope.launch {
            gameRepository.getScores {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = null
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        submissions = it.data.sortedByDescending {data ->
                            data.totalScore
                        }
                    )
                }
            }
        }
    }
}