package com.ketchupzzz.isaom.presentation.auth.edit_profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.presentation.auth.register.RegisterEvents

import com.ketchupzzz.isaom.ui.custom.PrimaryButton
import com.ketchupzzz.isaom.utils.IsaomTopBar
import kotlinx.coroutines.delay


@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    users: Users,
    state: EditProfileState,
    events: (EditProfileEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(key1 = users) {
        events.invoke(EditProfileEvents.OnNameChange(users.name?:""))

    }
    val context = LocalContext.current
    LaunchedEffect(key1 = state) {
        if (state.errors != null) {
            Toast.makeText(
                context,
                state.errors,
                Toast.LENGTH_SHORT
            ).show()
        }
        if (state.isDoneSaving != null) {

            Toast.makeText(
                context,
                state.isDoneSaving,
                Toast.LENGTH_SHORT
            ).show()
            delay(1000)
            navHostController.popBackStack()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        Text(text = "* Required", color = MaterialTheme.colorScheme.error)
        OutlinedTextField(value = state.name.value, onValueChange = {
            events.invoke(EditProfileEvents.OnNameChange(it))
        },
            modifier = modifier.fillMaxWidth(),
            isError = state.name.isError,
            label = {
                Text(text = "Fullname")
            },
            supportingText = {
                Text(
                    text =   state.name.errorMessage ?: "" ,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = modifier.weight(1f))
        PrimaryButton(onClick = { events.invoke(EditProfileEvents.OnSaveChanges(
            users.id ?: "",
            name = state.name.value,
        ))}, isLoading = state.isSaving) {
            Text(text = "Save")
        }
    }
}