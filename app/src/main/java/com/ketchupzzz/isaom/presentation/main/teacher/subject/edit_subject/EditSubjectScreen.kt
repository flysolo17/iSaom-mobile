package com.ketchupzzz.isaom.presentation.main.teacher.subject.edit_subject

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.ketchupzzz.isaom.ui.custom.PrimaryButton
import kotlinx.coroutines.delay


@Composable
fun EditSubjectScreen(
    id : String,
    state : EditSubjectState,
    events: (EditSubjectEvents) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            events.invoke(EditSubjectEvents.OnGetSubjectByID(id))
        }
    }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            events(EditSubjectEvents.OnCoverSelected(it))
        }
    }
    LaunchedEffect(state) {
        if (state.error !== null) {
            Toast.makeText(context,state.error, Toast.LENGTH_SHORT).show()
        }
        if (state.success !== null) {
            Toast.makeText(context,state.success, Toast.LENGTH_SHORT).show()
            delay(1000)
            navHostController.popBackStack()
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                .clickable {
                    imagePickerLauncher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            if (state.cover != null) {
                Image(
                    painter = rememberAsyncImagePainter(state.cover),
                    contentDescription = "Subject Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = state.subjects?.cover,
                    contentDescription = "${state.subjects?.name} cover",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        OutlinedTextField(
            value = state.name,
            onValueChange = {events(EditSubjectEvents.OnNameChanged(it))},
            label = { Text(text = "Enter subject name") },
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = modifier.weight(1f))
        PrimaryButton(onClick = {
            events.invoke(EditSubjectEvents.OnUpdateSubject(id, name = state.name, uri = state.cover))
        }, isLoading = state.isLoading) {
            Text(text = "Save Subject")
        }
    }

}