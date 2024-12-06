package com.ketchupzzz.isaom.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ketchupzzz.isaom.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GreetingMessage(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val languageService = remember { LanguageService(context) }
    val languages = listOf("ilo" to "Ilocano", "fil" to "Tagalog", "en" to "English")
    val coroutineScope = rememberCoroutineScope()

    var selectedLanguage by remember { mutableStateOf("en") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        selectedLanguage = languageService.getSavedLanguageFromPreferences()
        languageService.setAppLocale(selectedLanguage)
    }

    Column(
        modifier = modifier.padding(),
    ) {
        Text(text = stringResource(id = R.string.hello))
        Text(text = stringResource(id = R.string.one))

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = modifier
        ) {
            TextField(
                value = selectedLanguage,
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




class LanguageService(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    suspend fun saveLanguageToPreferences(language: String) {
        withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            editor.putString("language", language)
            editor.apply()
        }
    }

    // Retrieve the saved language from SharedPreferences asynchronously
    suspend fun getSavedLanguageFromPreferences(): String {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString("language", "en") ?: "en"
        }
    }

    // Set the app's locale based on the saved language asynchronously
    suspend fun setAppLocale(language: String) {
        withContext(Dispatchers.Main) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            Log.d("Language", "Locale set to: $language")

        }
    }
}
