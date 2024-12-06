package com.ketchupzzz.isaom.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
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
        modifier = modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        val languages = listOf("ilo" to "Ilocano", "fil" to "Tagalog", "en" to "English")
        val coroutineScope = rememberCoroutineScope()

        var selectedLanguage by remember { mutableStateOf("en") }
        var expanded by remember { mutableStateOf(false) }
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
                languages.forEach { (code,name)->
                    DropdownMenuItem(
                        text = { Text(name, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            expanded = false
                            selectedLanguage = code
                            coroutineScope.launch {
                                languageService.saveLanguageToPreferences(code)
                                languageService.setAppLocale(code)
                            }
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}