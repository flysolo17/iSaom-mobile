package com.ketchupzzz.isaom.presentation.main.students.subject.activities.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.firestore.index.IntMath
import com.ketchupzzz.isaom.IsaomApp
import com.ketchupzzz.isaom.R


@Composable
fun FinishActivityDialog(
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
                Box(
                    modifier = modifier.size(200.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        modifier = modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.act),
                        contentDescription = "Finish Activity"
                    )
                    Text(
                        "$score / $maxScore",
                        modifier = modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp,
                            color = Color.White
                        )
                    )
                }
                Spacer(
                    modifier = modifier.height(16.dp)
                )
                Button(
                    onClick = onDismiss
                ) { Text("Next") }
            }

        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun ScoreBoard() {
    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.act),
            contentDescription = "Finish Activity"
        )
        Text(
            "$19 / $20",
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 32.sp,
                color = Color.White
            )
        )
    }
}