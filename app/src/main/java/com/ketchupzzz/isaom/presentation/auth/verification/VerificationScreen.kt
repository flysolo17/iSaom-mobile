package com.ketchupzzz.isaom.presentation.auth.verification

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.presentation.routes.AppRouter

import com.ketchupzzz.isaom.ui.custom.PrimaryButton
import kotlinx.coroutines.delay

@Composable
fun VerificationScreen(
    modifier: Modifier = Modifier,
    state: VerificationState,
    events: (VerificationEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current


    LaunchedEffect(true) {
        events(VerificationEvents.OnSendVerification(context))
    }

    LaunchedEffect(state) {
        if (state.isVerified) {
            Toast.makeText(context, "Successfully Verified", Toast.LENGTH_SHORT).show()
            if (state.users != null) {

                navHostController.navigate(AppRouter.MainRoutes.route)
            }

        }
    }
    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.verification), contentDescription = "Verification")
            Spacer(modifier = modifier.height(12.dp))
            Text(text = "Verify your email address", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = "In order to start using Isaom App, you need to confirm your email address.",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                textAlign = TextAlign.Center,
                modifier = modifier.padding(8.dp)
            )
            Spacer(modifier = modifier.height(8.dp))

            PrimaryButton(
                onClick = { events(VerificationEvents.OnSendVerification(context)) },
                isLoading = state.isLoading,
                enabled = !state.isLoading && state.timer == 0
            ) {
                Text(
                    text = if (state.timer == 0) "Verify email address" else "${state.timer} seconds"
                )
            }
        }
    }

}
