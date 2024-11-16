package com.ketchupzzz.isaom.presentation.main



sealed interface MainEvents {
    data object OnGetUser : MainEvents
}