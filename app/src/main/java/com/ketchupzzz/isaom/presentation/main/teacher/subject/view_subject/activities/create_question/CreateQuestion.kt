package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.create_question

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity.ViewActivityEvents
import com.ketchupzzz.isaom.ui.custom.PrimaryButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestion(
    modifier: Modifier = Modifier,
    activityID : String ,
    question : Question ? = null,
    events: (ViewActivityEvents) -> Unit
) {
    var state by remember {
        mutableStateOf(CreateQuestionState())
    }
    val context = LocalContext.current
    question?.let {
        state = state.copy(
            title = it.title ?: "",
            desc = it.desc ?: "",
            uri = it.image?.toUri(),
            choices = it.choices,
            answer = it.answer ?: "",
            points = it.points,
        )
    }
    var showDialog by remember { mutableStateOf(false) }
    
    PrimaryButton(
        modifier = modifier.padding(8.dp),
        onClick = { showDialog = true }
    ) {
        Text(text = "Create Question",modifier = modifier.padding(8.dp))
    }
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Scaffold(
                topBar = {
                    val title = if (question == null) "Create" else "Update"
                    TopAppBar(
                        title = { Text(text = "$title Question") },
                        actions =  {
                            IconButton(onClick = {})
                            {
                                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Content")
                            }
                        },
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

                    //make the answer dropdown
                    //the list is choices
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
                        onValueChange = { num ->
                            state = state.copy(points = num.toIntOrNull() ?: 0)
                                        },
                        label = { Text("Points") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    PrimaryButton(
                        modifier = modifier.padding(16.dp),
                        isLoading = state.isLoading,
                        onClick = {
                            val newQuestion = question.generateNewQuestion(state)
                            events(ViewActivityEvents.OnSaveQuestion(actvityID = activityID, question = newQuestion, uri = state.uri) { uiState ->
                                when(uiState) {
                                    is UiState.Error -> {
                                        state = state.copy(isLoading = false)
                                        Toast.makeText(context,uiState.message,Toast.LENGTH_SHORT).show()
                                    }
                                    is UiState.Loading -> state = state.copy(isLoading = true)
                                    is UiState.Success -> {
                                        state = state.copy(isLoading = false)
                                        Toast.makeText(context,uiState.data,Toast.LENGTH_SHORT).show()
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

fun Question?.generateNewQuestion(state : CreateQuestionState) : Question{
    val question = this
    val newQuestion = question?.copy(
        title = question.title ,
        desc = question.desc,
        image = question.image,
        choices = question.choices,
        answer = question.answer,
        points = question.points,
    )
        ?: Question(
            title = state.title ,
            desc = state.desc,
            image = "",
            choices = state.choices,
            answer = state.answer,
            points = state.points,
        )
    return newQuestion
}

@Composable
fun CreateActionButton(
    modifier: Modifier = Modifier,
    label : String,
    onCreate : (action : String) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                modifier = modifier.weight(1f),
                value = text,
                label = { Text(text = label)},
                onValueChange = {text = it},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            FilledTonalIconButton(
                onClick = { onCreate(text)
                text = ""
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Action")
            }
        }
    }

}


@Composable
fun CardContainer(
    modifier: Modifier = Modifier,
    text : String,
    onDelete : (text : String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.elevatedCardElevation(
            0.dp
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, style = MaterialTheme.typography.titleMedium)
            IconButton(
                modifier = modifier.size(24.dp),
                onClick = { onDelete(text) },
            ) {
                Icon(painter = painterResource(id = R.drawable.icon_x), contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Composable
fun AddImage(
    modifier: Modifier = Modifier,
    selectedImage : Uri?,
    onImageSelected : (uri : Uri ?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
       onImageSelected(uri)
    }

    Column(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Add Image")
            IconButton(onClick = {
                launcher.launch("image/*")
            }) {
                Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = "Add Image")

            }
        }
        selectedImage?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Image",
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}