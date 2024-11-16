package com.ketchupzzz.isaom.presentation.main.translator

import android.content.Context
import android.net.Uri
import com.ketchupzzz.isaom.models.SourceAndTargets


sealed interface TranslatorEvents  {
    data class OnTextChanged(val text: String) : TranslatorEvents
    data class OnTranslateText(val text : String, val source : SourceAndTargets, val target : SourceAndTargets) :
        TranslatorEvents

    data class OnSourceChanged(val source: SourceAndTargets) : TranslatorEvents
    data class OnTargetChanged(val target: SourceAndTargets) : TranslatorEvents
    data class OnSwitchLanguage(val source : SourceAndTargets, val target: SourceAndTargets) :
        TranslatorEvents


    data class OnTransformImageToText(val context : Context, val uri : Uri) : TranslatorEvents
    data object OnGetAllTranslationHistory : TranslatorEvents
}