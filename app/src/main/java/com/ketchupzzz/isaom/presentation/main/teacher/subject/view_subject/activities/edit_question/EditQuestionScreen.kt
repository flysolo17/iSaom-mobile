package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.edit_question

import android.widget.Toast
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.create_question.AddImage
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.create_question.CardContainer
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.create_question.CreateActionButton
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity.ViewActivityEvents
import com.ketchupzzz.isaom.ui.custom.PrimaryButton
import com.ketchupzzz.isaom.utils.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestionScreen(
    modifier: Modifier = Modifier,
    button : Modifier,
    activityID : String,
    question : Question,
    events: (ViewActivityEvents) -> Unit
) {

    var state by remember {
        mutableStateOf(EditQuestionState())
    }
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        state = state.copy(
            title = question.title ?: "",
            desc = question.desc ?: "",
            choices = question.choices,
            answer = question.answer ?: "",
            points = question.points ,
        )
    }
    val context = LocalContext.current
    Button(onClick = { showDialog = true }, modifier = button) {
        Text(text = "Edit")
    }
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Scaffold(
                topBar = {

                    TopAppBar(
                        title = { Text(text = "Update Question") },

                        navigationIcon = {
                            IconButton(onClick = { showDialog = false }) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    //create form
                    TextField(
                        value = state.title,
                        onValueChange = { state = state.copy(title = it) },
                        label = { Text("Title") },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    CreateActionButton(
                        label = "Add Choice"
                    ) { text ->
                        val currentChoices : List<String> = state.choices
                        state = state.copy(
                            choices = currentChoices + text
                        )
                    }

                    Column {
                        if (state.choices.isNotEmpty()) {
                            Text(text = "Choices", style = MaterialTheme.typography.titleMedium,modifier = modifier.padding(8.dp))
                        }
                        state.choices.forEach {
                            CardContainer(text = it) {choice ->
                                val currentChoices: List<String> = state.choices
                                state = state.copy(
                                    choices = currentChoices.filter { it != choice }
                                )
                            }
                        }
                    }

                    AddImage(selectedImage = state.uri) {
                        state = state.copy(uri = it)
                    }

                    val choices = state.choices


                    ExposedDropdownMenuBox(
                        expanded = state.expanded,
                        onExpandedChange = { state = state.copy(expanded = !state.expanded) }
                    ) {
                        TextField(
                            value = state.answer,
                            onValueChange = { state = state.copy(answer = it) },
                            label = { Text("Answer") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = state.expanded,
                            onDismissRequest = { state = state.copy(expanded = false) }
                        ) {
                            choices.forEach { choice ->
                                DropdownMenuItem(
                                    text = { Text(choice) },
                                    onClick = {
                                        state = state.copy(answer = choice)
                                        state = state.copy(expanded = false)
                                    }
                                )
                            }
                        }
                    }
                    TextField(
                        value = state.points.toString(),
                        onValueChange = { state = state.copy(points = it.toIntOrNull() ?: 0) },
                        label = { Text("Points") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    PrimaryButton(
                        modifier = modifier,
                        isLoading = state.isLoading,
                        onClick = {
                          val  new =  question.copy(
                                title = state.title ,
                                desc = state.desc,
                                choices = state.choices,
                                answer = state.answer,
                                points = state.points,
                            )
                            events(ViewActivityEvents.OnSaveQuestion(actvityID = activityID, question =new , uri = state.uri) { uiState ->
                                when(uiState) {
                                    is UiState.Error -> {
                                        state = state.copy(isLoading = false)
                                        Toast.makeText(context,uiState.message, Toast.LENGTH_SHORT).show()
                                    }
                                    is UiState.Loading -> state = state.copy(isLoading = true)
                                    is UiState.Success -> {
                                        state = state.copy(isLoading = false)
                                        Toast.makeText(context,uiState.data, Toast.LENGTH_SHORT).show()
                                        showDialog =false
                                    }
                                }
                            })
                        },
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}