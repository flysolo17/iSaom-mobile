package com.ketchupzzz.isaom.presentation.main.game

import androidx.compose.animation.fadeIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventStart
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.presentation.main.game.GameEvents
import com.ketchupzzz.isaom.presentation.main.game.GameState
import com.ketchupzzz.isaom.repository.game.GameRepository
import com.ketchupzzz.isaom.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class GameViewModel @Inject constructor(
     private val gameRepository: GameRepository
) : ViewModel() {
     var state by mutableStateOf(GameState())

    init {
        events(GameEvents.OnGetGames)
    }
     fun events(e : GameEvents) {
          when(e) {
               GameEvents.OnGetGames -> getAllGames()
          }
     }

     private fun getAllGames() {
          viewModelScope.launch {
               gameRepository.getAllGames {
                   state=  when(it) {
                         is UiState.Error -> state.copy(
                              isLoading = false,
                              errors = it.message
                         )
                         UiState.Loading -> state.copy(
                              isLoading = true,
                              errors = null
                         )
                         is UiState.Success -> state.copy(
                              isLoading = false,
                              errors = null,
                              games = it.data
                         )
                    }
               }
          }
     }


}