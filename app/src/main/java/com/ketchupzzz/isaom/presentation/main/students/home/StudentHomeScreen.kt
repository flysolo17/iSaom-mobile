package com.ketchupzzz.isaom.presentation.main.students.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.ui.custom.SubjectCard
import com.ketchupzzz.isaom.utils.toast

@Composable
fun StudentHomeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    state: StudentHomeState,
    events: (StudentHomeEvents) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.users != null) {
            events(StudentHomeEvents.OnGetSubjects(state.users.id ?:""))
        }
        if (state.error != null) {
            context.toast(state.error)
        }
    }
    if (state.subjectFound != null) {
        JoinClassAlertDialog(state.subjectFound, onConfirm = {
            events.invoke(StudentHomeEvents.OnJoinSubject(state.users?.id?:"", subjectID = state.subjectFound?.id ?: ""))
        },
            onDismiss = {
                events.invoke(StudentHomeEvents.UpdateSubjectFound(null))
            })
    }
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("My  subjects ${state.subjects.size}", style = MaterialTheme.typography.titleLarge)
        }
        item {
            JoinSubjectButton {
                events.invoke(StudentHomeEvents.OnFindSubjects(it))
            }
        }

        items(
            state.subjects,
        ) { subject ->
            SubjectCard(subject = subject,
                onClick = {
                    navHostController.navigate(AppRouter.StudentViewSubject.createRoute(subject))
                }
            ) {}
        }
    }
}
@Composable
fun JoinSubjectButton(
    modifier: Modifier = Modifier,
    onJoin : (String) -> Unit
) {
    var code by remember {
        mutableStateOf("")
    }
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = code,
                onValueChange = { code = it },
                modifier = modifier.weight(1f),
                singleLine = true,

                textStyle = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    if (code.isEmpty()) {
                        Text(
                            text = "Enter code",
                        )
                    }
                    innerTextField()
                }
            )
            FilledIconButton(
                onClick = { onJoin(code) }) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
            }
        }
    }
}

@Composable
fun JoinClassAlertDialog(subject : Subjects , onConfirm: () -> Unit, onDismiss: () -> Unit) {
    var openDialog by remember { mutableStateOf(true) }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
                onDismiss()
            },
            title = {
                Text("Join Class")
            },
            text = { Text(text = "Are you sure you want to join ${subject.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                    openDialog = false
                    onConfirm()
                }) {
                    Text("Join")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog = false
                    onDismiss()
                }) {
                    Text("Cancel")
                }
            },

        )
    }
}