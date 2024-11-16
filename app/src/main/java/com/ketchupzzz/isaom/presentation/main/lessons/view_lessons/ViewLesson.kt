package com.ketchupzzz.isaom.presentation.main.lessons.view_lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.SignLanguageLesson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun ViewLessonScreen(
    modifier: Modifier = Modifier,
    lesson: SignLanguageLesson,
    navHostController: NavHostController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            lesson.title ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Text(lesson.desc ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier.padding(8.dp)
        )
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // Adjust height as needed
            factory = { ctx ->
                YouTubePlayerView(ctx).apply {
                    lifecycleOwner.lifecycle.addObserver(this)
                    addYouTubePlayerListener(
                        object  : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.loadVideo(lesson.videoId!!,0f)
                                youTubePlayer.pause()
                                super.onReady(youTubePlayer)
                            }
                        }
                    )
                }
            }
        )
    }
}

