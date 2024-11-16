package com.ketchupzzz.isaom.presentation.main.teacher.subject.add_subject

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ketchupzzz.isaom.presentation.main.translator.shareText
import com.ketchupzzz.isaom.ui.custom.PrimaryButton
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSubjectScreen(
    modifier: Modifier = Modifier,
    state: AddSubjectState,
    events: (AddSubjectEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            events(AddSubjectEvents.OnCoverSelected(it))
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(state) {
        if (state.error !== null) {
            Toast.makeText(context,state.error,Toast.LENGTH_SHORT).show()
        }
        if (state.success !== null) {
            Toast.makeText(context,state.success,Toast.LENGTH_SHORT).show()
            delay(1000)
            showDialog = true
        }
    }
    if (showDialog) {
        FullScreenDialog(code = state.code) {
            showDialog = !showDialog
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
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Pick Image",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        OutlinedTextField(
            value = state.name,
            onValueChange = {events(AddSubjectEvents.OnNameChanged(it))},
            label = { Text(text = "Enter subject name")},
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = modifier.height(8.dp))
        OutlinedTextField(
            value = state.code,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Code")},
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = modifier.height(8.dp))

        val sections = state.sections
        var sectionExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(expanded = sectionExpanded, onExpandedChange =  {
            sectionExpanded = !sectionExpanded
        }) {
            OutlinedTextField(
                readOnly = true,
                value = state.selectedSection?.name ?: "No Section Selected",
                onValueChange = { events(AddSubjectEvents.OnSectionSelected(state.selectedSection)) },
                label = { Text("Select Section") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sectionExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                isError = state.selectedSection == null,
                supportingText = {
                    Text(
                        text = if( state.selectedSection == null ) "No section selected" else "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Start
                    )
                },
            )

            ExposedDropdownMenu(
                expanded = sectionExpanded,
                onDismissRequest = { sectionExpanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                sections.forEach { section ->
                    DropdownMenuItem(
                        text = { Text(section.name?: "no section") },
                        onClick = {
                            events(AddSubjectEvents.OnSectionSelected(section))
                            sectionExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = modifier.weight(1f))
        PrimaryButton(onClick = {
            if(state.selectedSection !== null && !state.isLoading) {
                events.invoke(AddSubjectEvents.OnAddingSubject(state.selectedSection.id ?: ""))
            } else {
                Toast.makeText(context,"No Section Selected",Toast.LENGTH_SHORT).show()
            }
        }, isLoading = state.isLoading) {
            Text(text = "Create Subject")
        }
    }
}


@Composable
fun FullScreenDialog(
    modifier: Modifier = Modifier,
    code : String,
    onDismissRequest: () -> Unit,

) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ){
                    IconButton(onClick = {
                        onDismissRequest()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
                Text(
                    "Your code has been generated!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = modifier.height(8.dp))
                Box(
                    modifier = modifier
                        .wrapContentSize()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        code,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
                Spacer(modifier = modifier.height(8.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA500),
                        contentColor = Color.White
                    ),
                    onClick = {
                        shareText(context = context, text = code)
                    }
                ) {
                    Text("Copy Code")
                }
            }
        }
    }
}
