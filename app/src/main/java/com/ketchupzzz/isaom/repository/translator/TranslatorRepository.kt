package com.ketchupzzz.isaom.repository.translator

import android.content.Context
import android.net.Uri
import com.ketchupzzz.isaom.models.history.TranslatorHistory
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.services.TranslationResponse

interface TranslatorRepository {
     fun translateText(
          text : String,
          source : String,
          target : String,
          result : (UiState<TranslationResponse>) -> Unit
     )
     fun translateImage(
          context : Context,
         uri : Uri,
          source: String,
          target: String
     )

     suspend fun saveTranslator(translatorHistory: TranslatorHistory)
     suspend fun getAllTranslationHistory(
         limit : Int ? = null,
         result: (UiState<List<TranslatorHistory>>) -> Unit
     )
}