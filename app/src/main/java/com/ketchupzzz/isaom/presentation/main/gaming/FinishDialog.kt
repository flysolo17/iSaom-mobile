package com.ketchupzzz.isaom.presentation.main.gaming


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.games.GameSubmission


@Composable
fun FinishDialog(
    modifier: Modifier = Modifier,
    score : Int,
    maxScore : Int,
    onDismiss : () -> Unit
) {

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ){
        Surface(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = modifier.size(210.dp),
                    painter = painterResource(R.drawable.over),
                    contentDescription = "Game Over"
                )
                Spacer(
                    modifier = modifier.height(16.dp)
                )
                Text("${score} / ${maxScore}", style = MaterialTheme.typography.titleLarge)
                Spacer(
                    modifier = modifier.height(16.dp)
                )
                Button(
                    onClick = onDismiss
                ) {
                    Text("Close")
                }
            }
        }
    }
}