package com.ketchupzzz.isaom.presentation.auth.register.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.internal.AccountType
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.presentation.auth.register.RegisterEvents
import com.ketchupzzz.isaom.presentation.auth.register.RegisterState
import com.ketchupzzz.isaom.ui.custom.PrimaryButton


@Composable
fun AccountTypeScreen(
    modifier: Modifier = Modifier,
    accountType: (UserType) -> Unit
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            modifier = Modifier
                .width(150.dp)
                .height(150.dp),
            contentDescription = stringResource(R.string.logo)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "I am a ",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF6600),
                contentColor = Color.White
            )
            ,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    accountType.invoke(UserType.STUDENT)
                }
        ) {
            Text(
                text = "Student",
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary),

            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    accountType.invoke(UserType.TEACHER)
                }
        ) {

                Text(
                    text = "Teacher",
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
        }
    }
}