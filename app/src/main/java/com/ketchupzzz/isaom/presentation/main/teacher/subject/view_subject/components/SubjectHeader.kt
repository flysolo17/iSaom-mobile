package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.presentation.main.teacher.subject.shared.DeleteConfirmationDialog
import com.ketchupzzz.isaom.presentation.routes.AppRouter

import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectEvents
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectState

@Composable
fun SubjectHeader(
    subjects: Subjects,
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    events: (ViewSubjectEvents) -> Unit,
    state : ViewSubjectState
) {
    var expanded by remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .height(200.dp)
    ) {
        AsyncImage(
            model = subjects.cover,
            contentDescription = "${subjects.name} cover",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val color = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
                Box {
                    IconButton(onClick = { expanded = !expanded }, colors = color) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Settings")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit"
                                )
                            },
                            text = { Text("Edit Subject") },
                            onClick = { navHostController.navigate(AppRouter.EditSubject.createRoute(subjects?.id ?: "")) }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Edit"
                                )
                            },
                            text = { Text("Delete Subject") },
                            onClick = {
                                openAlertDialog.value = true
                            }
                        )

                    }
                }

            }
            Text(text = subjects.name ?: "", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }

    }
    if (openAlertDialog.value) {
        DeleteConfirmationDialog(title = "Delete Subject",
            message = "Are you sure you want to delete ${subjects.name} ? " +
                    "By doing so all modules , activities and submissions that is related to this subject is also be deleted",
            onConfirm = {
                events.invoke(ViewSubjectEvents.OnDeleteSubject(subjects))
                openAlertDialog.value = false
            },
            onDismiss = { openAlertDialog.value = false}
        )
    }

}