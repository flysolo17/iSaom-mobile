package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HtmlWindow(
    modifier: Modifier = Modifier,
    html : String
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                settings.javaScriptEnabled = true
                loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)

            }
        },
        modifier = modifier.fillMaxWidth().background(
            color = MaterialTheme.colorScheme.background
        )
    )
}