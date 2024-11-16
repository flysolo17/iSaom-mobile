package com.ketchupzzz.isaom.presentation.main.profle

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.ui.custom.PrimaryButton
import com.ketchupzzz.isaom.utils.ProfileImage

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,events: (ProfileEvents) -> Unit,
    navHostController: NavHostController,
    mainNav : NavHostController
) {
    val  context = LocalContext.current
    LaunchedEffect(state) {
        if (state.isLoggedOut) {
            Toast.makeText(context,"Successfully Logged Out",Toast.LENGTH_SHORT).show()
            mainNav.navigate(AppRouter.AuthRoutes.route)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileLayout(state = state)
        Spacer(modifier = modifier.weight(1f))
        PrimaryButton(onClick = { navHostController.navigate(AppRouter.EditProfileRoute.navigate(state.users!!)) }) {
            Text(text = "Edit Profile")
        }
        PrimaryButton(onClick = { navHostController.navigate(AppRouter.ChangePassword.route) }) {
            Text(text = "Change Password")
        }
        PrimaryButton(onClick = { events(ProfileEvents.OnLoggedOut) }, isLoading = state.isLoading) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun ProfileLayout(
    modifier: Modifier = Modifier,
    state: ProfileState,
) {
    val users = state.users
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(imageURL = users?.avatar ?: "", size = 80.dp) {
            
        }
        Text(text = "${users?.name}", style =MaterialTheme.typography.titleLarge)
        Text(text = "${users?.type?.name}", style =MaterialTheme.typography.labelSmall)
    }
}
