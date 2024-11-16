package com.ketchupzzz.isaom.presentation.main.translator

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.ketchupzzz.isaom.models.SourceAndTargets
import com.ketchupzzz.isaom.models.history.TranslatorHistory

import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.repository.translator.TranslatorRepository
import com.ketchupzzz.isaom.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel

class TranslatorViewModel @Inject constructor(
     private val translatorRepository: TranslatorRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(TranslatorState())
    init {
        state = state.copy(
            users = authRepository.getUsers()
        )
        events(TranslatorEvents.OnGetAllTranslationHistory)
    }
    fun events(events: TranslatorEvents) {
        when(events) {
            is TranslatorEvents.OnTextChanged -> {
                state = state.copy(
                    text = events.text
                )
            }
            is TranslatorEvents.OnTranslateText -> translateText(events.text,events.source,events.target)
            is TranslatorEvents.OnSourceChanged -> state = state.copy(
                source = events.source
            )
            is TranslatorEvents.OnTargetChanged -> state = state.copy(
                target = events.target
            )

            is TranslatorEvents.OnSwitchLanguage -> state = state.copy(
                source = events.target,
                target = events.source,
            )

            is TranslatorEvents.OnTransformImageToText -> transformImageToText(events.context,events.uri)
            TranslatorEvents.OnGetAllTranslationHistory -> getAllHistory()
        }
    }

    private fun getAllHistory() {
        viewModelScope.launch {
            translatorRepository.getAllTranslationHistory {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        history = it.data
                    )
                }
            }
        }
    }

    private fun transformImageToText(context: Context, uri: Uri) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image: InputImage
        try {
            image = InputImage.fromFilePath(context, uri)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    state = state.copy(
                        text = visionText.text
                    )
                    events(TranslatorEvents.OnTranslateText(
                        state.text,
                        state.source,
                        state.target
                    ))
                }
                .addOnFailureListener { e ->
                    state = state.copy(errors = e.message)
                }
        } catch (e: IOException) {
            state = state.copy(errors = e.message)
        }
    }

    private fun translateText(text: String, source: SourceAndTargets, target: SourceAndTargets) {
        viewModelScope.launch {
            translatorRepository.translateText(text,source.code,target.code) { uiState ->
                state = when (uiState) {
                    is UiState.Loading -> {
                        state.copy(isLoading = true, errors = null, translation = "Loading..")
                    }
                    is UiState.Error -> {
                        state.copy(isLoading = false, errors = uiState.message, translation = uiState.message)
                    }
                    is UiState.Success -> {
                        saveTranslation(text, translation = uiState.data.translation_text, source, target)
                        state.copy(
                            isLoading = false,
                            translation = uiState.data.translation_text,
                            errors = null
                        )

                    }
                }
            }
        }
    }

    private fun saveTranslation(text : String,translation : String,source: SourceAndTargets, target: SourceAndTargets) {
        val translatorHistory = TranslatorHistory(
            userID = state.users?.id,
            text = text,
            translation = translation,
            source = source,
            target = target
        )
        viewModelScope.launch {
            translatorRepository.saveTranslator(translatorHistory)
        }

    }
}