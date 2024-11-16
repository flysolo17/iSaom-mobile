package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import com.ketchupzzz.isaom.ui.custom.PrimaryButton

@Composable
fun FullScreenNoSubject(modifier: Modifier = Modifier, navHostController: NavHostController) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NoSubject(navHostController = navHostController)
    }
}

@Composable
fun NoSubject(navHostController: NavHostController) {
    Text(text = "No Subject Found!")
    Spacer(modifier = Modifier.height(16.dp))
    PrimaryButton(onClick = { navHostController.popBackStack() }) {
        Text(text = "Back")
    }
}