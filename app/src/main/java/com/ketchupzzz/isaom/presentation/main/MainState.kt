package com.ketchupzzz.isaom.presentation.main

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import com.ketchupzzz.isaom.models.Users

data class MainState(
    val isLoading : Boolean = false,
    val users : Users ?= null,
    val errors : String ? =null,
    val message : String ? = null
)