package com.ketchupzzz.isaom.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.utils.LanguageService
import kotlinx.coroutines.launch


fun String.getSelectedLanguage() : String {
    return if (this == "ilo") {
        "Ilocano"
    } else if (this == "fil") {
        "Tagalog"
    } else {
        "English"
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        val languages = listOf("ilo" to "Ilocano", "fil" to "Tagalog", "en" to "English")
        val coroutineScope = rememberCoroutineScope()

        var selectedLanguage by remember { mutableStateOf("en") }
        var expanded by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }  // To show/hide the confirmation dialog
        val languageService = remember { LanguageService(context) }

        LaunchedEffect(Unit) {
            selectedLanguage = languageService.getSavedLanguageFromPreferences()
            languageService.setAppLocale(selectedLanguage)
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = modifier
        ) {
            TextField(
                value = selectedLanguage.getSelectedLanguage(),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true, // Ensures the TextField is non-editable
                label = { Text("Select Language") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { Text(name, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            expanded = false
                            if (selectedLanguage != code) {
                                selectedLanguage = code
                                showDialog = true  // Show confirmation dialog if language is changing
                            }
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        // Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(R.string.confirm)) },
                text = { Text(stringResource(R.string.are_you_sure_you_want_to_change_the_language)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                languageService.saveLanguageToPreferences(selectedLanguage)
                                languageService.setAppLocale(selectedLanguage)
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }
    }
}
