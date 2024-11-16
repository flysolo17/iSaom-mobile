package com.ketchupzzz.isaom.presentation.main.lessons

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.SignLanguageLesson
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.utils.generateRandomString
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun LessonScreen(
    modifier: Modifier = Modifier,
    state: LessonState,
    events: (LessonEvents) -> Unit,
    navHostController: NavHostController
) {

    Column(modifier = modifier
        .fillMaxSize()
        .padding(8.dp)) {
        LazyColumn {
            items(state.lessons, key = {it.id ?: generateRandomString() }) {
                LessonCard(lesson = it, onClick = {
                    navHostController.navigate(AppRouter.ViewSignLanguageLessons.navigate(it))
                })
            }
        }
    }
}

@Composable
fun LessonCard(modifier: Modifier =Modifier,lesson: SignLanguageLesson ,onClick : () -> Unit) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(4.dp)
        .clickable { onClick() }
    ) {
            Row(
                modifier = modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Column(
                  modifier = modifier.fillMaxWidth().weight(1f)
              ) {
                  Text(text = lesson.title!!, style = MaterialTheme.typography.titleMedium)
                  Text(text = lesson.desc!!, style = MaterialTheme.typography.labelMedium)
              }
                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = "HHAHA"
                )
            }


    }
}

