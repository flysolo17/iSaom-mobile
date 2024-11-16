package com.ketchupzzz.isaom.presentation.main.leaderboard



sealed interface LeaderboardEvents {
    data object OnGetLeaderboard : LeaderboardEvents
}