package com.ketchupzzz.isaom

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import com.ketchupzzz.isaom.utils.LanguageService

import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


@HiltAndroidApp
class IsaomApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val languageService = LanguageService(this)
        CoroutineScope(Dispatchers.Main).launch {
            val savedLanguage = languageService.getSavedLanguageFromPreferences()
            languageService.setAppLocale(savedLanguage)
        }
    }

}
