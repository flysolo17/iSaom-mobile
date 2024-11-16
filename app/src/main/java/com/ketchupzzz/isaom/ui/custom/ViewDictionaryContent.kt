package com.ketchupzzz.isaom.ui.custom

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ketchupzzz.isaom.models.dictionary.remote.Dictionary


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewDialog(
    modifier: Modifier = Modifier,
    dictionary: Dictionary,
    onDismiss : () -> Unit
) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { onDismiss() }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Isaom App") },
                        navigationIcon = {
                            IconButton(onClick = onDismiss) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ){
                AndroidView(
                    factory = {
                        WebView(it).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            loadUrl(dictionary.link ?: "")
                        }
                    },
                    modifier = modifier.fillMaxSize().padding(it)
                )
            }


    }
}
