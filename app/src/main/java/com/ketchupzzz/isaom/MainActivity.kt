package com.ketchupzzz.isaom

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.ketchupzzz.isaom.presentation.main.MainScreen
import com.ketchupzzz.isaom.presentation.main.MainViewModel
import com.ketchupzzz.isaom.presentation.main.dictionary.DictionaryScreen
import com.ketchupzzz.isaom.presentation.main.dictionary.DictionaryViewModel

import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.presentation.routes.authNavGraph


import com.ketchupzzz.isaom.ui.theme.ISaomTheme
import com.ketchupzzz.isaom.utils.GreetingMessage
import com.ketchupzzz.isaom.utils.LanguageService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestAudioPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                initializeApp()
            } else {
                // Close the app if permission is denied
                Toast.makeText(this, "Audio permission required. Closing app.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isAudioPermissionGranted()) {
            initializeApp()
        } else {
            requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun initializeApp() {
        enableEdgeToEdge()
        setContent {
            ISaomTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                val context = LocalContext.current
                val languageService = remember { LanguageService(context) }
                LaunchedEffect(Unit) {
                    val savedLanguage = languageService.getSavedLanguageFromPreferences()
                    languageService.setAppLocale(savedLanguage)
                }
                IsaomApp(windowSize)
            }
        }
    }

    private fun isAudioPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun IsaomApp(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRouter.AuthRoutes.route) {
        authNavGraph(navController)
        composable(route = AppRouter.MainRoutes.route) {
            val viewModel = hiltViewModel<MainViewModel>()
            MainScreen(
                state = viewModel.state,
                events = viewModel::events,
                mainNav = navController
            )
        }
    }
}