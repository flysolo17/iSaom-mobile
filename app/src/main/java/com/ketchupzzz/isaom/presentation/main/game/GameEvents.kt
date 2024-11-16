package com.ketchupzzz.isaom.presentation.main.game



sealed interface GameEvents {
    data object OnGetGames : GameEvents
}