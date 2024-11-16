package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.presentation.main.teacher.subject.shared.DeleteConfirmationDialog
import com.ketchupzzz.isaom.presentation.routes.AppRouter

import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectEvents
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectState
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components.BottomNavigationItems
import com.ketchupzzz.isaom.ui.custom.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityPage(
    modifier: Modifier = Modifier,
    state: ViewSubjectState,
    events: (ViewSubjectEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state.subjectID) {
        if (state.subjectID.isNotEmpty()) {
            events.invoke(ViewSubjectEvents.OnGetActivitiesBySubjectID(state.subjectID))
        }
    }
    var createActivitySheet by remember { mutableStateOf(false) }
    val activitySheet = rememberModalBottomSheetState()
    if (createActivitySheet) {
        ActivityForm(
            sheetState = activitySheet,
            isLoading = state.isActivityLoading,
            onDismiss = { createActivitySheet = !createActivitySheet },
            events = events
        )
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            Button(
                onClick = { createActivitySheet = !createActivitySheet },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(4.dp)

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Crete Activity")
                    Text(text = "Create Activity")
                }
            }
        }
        items(state.activities,key = {it.id!!}) {
            ActivityCard(
                activity = it, navHostController = navHostController,
                onDelete = {actvityID -> events.invoke(ViewSubjectEvents.deleteActivity(actvityID, context = context)) },
                onUpdateLock = {activity-> events.invoke(ViewSubjectEvents.updateActivityLock(activity, context)) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityForm(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    sheetState : SheetState,
    onDismiss : ()-> Unit,
    events: (ViewSubjectEvents) -> Unit
) {
    var state by remember {
        mutableStateOf(ActivityPageState(isLoading = isLoading))
    }
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Create Activity", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = modifier.height(16.dp))
            TextField(
                value = state.title,
                onValueChange = {state = state.copy(title = it)},
                label = { Text(text = "Enter Title")},
                modifier = modifier.fillMaxWidth()
            )

            TextField(
                value = state.desc,
                onValueChange = {state = state.copy(desc = it)},
                label = { Text(text = "Enter Description")},
                modifier = modifier.fillMaxWidth()
            )

            PrimaryButton(onClick = {
                val activity  = Activity(
                    title = state.title,
                    desc = state.desc
                )
                events.invoke(ViewSubjectEvents.OnCreateActivity(activity) {
                    when(it) {
                        is UiState.Error -> {
                            state = state.copy(isLoading = false)
                            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> state = state.copy(isLoading = true)
                        is UiState.Success -> {
                            state = state.copy(isLoading = false)
                            Toast.makeText(context,it.data,Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                    }
                })
            }, isLoading = state.isLoading
            ) {
                Text(text = "Create Activity")
            }

        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCard(
    modifier: Modifier = Modifier,
    activity: Activity,
    navHostController: NavHostController,
    onDelete : (activityID: String) -> Unit,
    onUpdateLock : (actvity : Activity) -> Unit
) {

    var openAlertDialog by remember { mutableStateOf(false) }
    if (openAlertDialog) {
        DeleteConfirmationDialog(
            title = "Delete Activity ?",
            message = "Are you sure you want to delete ${activity.title}",
            onConfirm = {  onDelete(activity.id?: "") },
            onDismiss = {
                openAlertDialog = false
            }
        )
    }

    var moduleSettingsSheet by remember { mutableStateOf(false) }
    val settingsSheetState = rememberModalBottomSheetState()
    if (moduleSettingsSheet) {
        ModalBottomSheet(
            onDismissRequest = { moduleSettingsSheet = !moduleSettingsSheet },
            sheetState = settingsSheetState
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${activity.title}", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${activity.desc}",
                    modifier = modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )
                BottomNavigationItems(title = "View", icon = Icons.Default.Visibility) {
                    navHostController.navigate(AppRouter.ViewActivity.createRoute(activityID = activity.id!!))
                    moduleSettingsSheet = false
                }

                BottomNavigationItems(title = "Delete", icon = Icons.Default.Delete) {
                    openAlertDialog = true
                }



                val isLocked= !activity.open
                val lockTitle = if (isLocked) "Unlock" else "Lock"
                val lockIcon = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen
                BottomNavigationItems(title = lockTitle, icon = lockIcon) {
                    onUpdateLock(activity)
                }
            }
        }
    }
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            moduleSettingsSheet = !moduleSettingsSheet
        }
    ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = modifier
                .fillMaxSize()
                .weight(1f)) {
                Text(text = "${activity.title}", style = MaterialTheme.typography.titleMedium)
                Text(text = "${activity.desc}", style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                imageVector = if (activity.open) Icons.Default.LockOpen  else Icons.Default.Lock,
                contentDescription = "Delete"
            )
        }
    }
}

