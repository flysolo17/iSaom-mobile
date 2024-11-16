package com.ketchupzzz.isaom.presentation.main.about

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.history.initHistories
import com.ketchupzzz.isaom.utils.IsaomTopBar


@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val history = context.initHistories()
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        items(history) {
            AboutIlocanoCard(history = it) {

            }
        }
    }


}