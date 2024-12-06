package com.ketchupzzz.isaom.presentation.main.lessons

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.Difficulty
import com.ketchupzzz.isaom.models.SectionedLessons
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
            if(state.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
            if(state.errors != null) {
                item {
                    Text("${state.errors}")
                }
            }
//
            val beginner = state.lessons.filter { it.dificulty == Difficulty.BEGINNER }
            val intermediate = state.lessons.filter { it.dificulty == Difficulty.INTERMEDIATE }
            val advanced = state.lessons.filter { it.dificulty == Difficulty.ADVANCED }
            item {
                SectionedLessonCard(
                    enabled = true,
                    finished = state.lessonAccount?.lessons ?: emptyList(),
                    label = "Beginner",
                    lessons = beginner
                ) {
                    if (state.lessonAccount?.lessons?.contains(it.id) ==false) {
                        events.invoke(LessonEvents.OnUpdate(it.id!!))
                    }
                    navHostController.navigate(AppRouter.ViewSignLanguageLessons.navigate(it))
                }
            }
            item {
                val isBeginnerFinished = beginner.map { it.id!! }
                SectionedLessonCard(
                    enabled = state.lessonAccount?.lessons?.containsAll(isBeginnerFinished) ?: false,
                    label = "Intermediate",
                    finished = state.lessonAccount?.lessons ?: emptyList(),
                    lessons = intermediate
                ) {
                    if (state.lessonAccount?.lessons?.contains(it.id) ==false) {
                        events.invoke(LessonEvents.OnUpdate(it.id!!))
                    }
                    navHostController.navigate(AppRouter.ViewSignLanguageLessons.navigate(it))
                }
            }
            item {
                val inter = intermediate.map { it.id!! }
                SectionedLessonCard(
                    enabled = state.lessonAccount?.lessons?.containsAll(inter) ?: false,
                    label = "Advanced",
                    finished = state.lessonAccount?.lessons ?: emptyList(),
                    lessons = advanced
                ) {
                    if (state.lessonAccount?.lessons?.contains(it.id) ==false) {
                        events.invoke(LessonEvents.OnUpdate(it.id!!))
                    }
                    navHostController.navigate(AppRouter.ViewSignLanguageLessons.navigate(it))
                }
            }
        }
    }
}

@Composable
fun SectionedLessonCard(
    modifier: Modifier = Modifier,
    enabled : Boolean,
    label : String,
    finished : List<String>,
    lessons : List<SignLanguageLesson>,
    onClick: (SignLanguageLesson) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val progress = mutableListOf<String>()
    val data = lessons.forEach {
        if (finished.contains(it.id)) {
            progress.add(it.id!!)
        }
    }
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp),
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                ),
                headlineContent = { Text("${label}") },
                trailingContent = {
                    if (!enabled) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Lock"
                        )
                    } else {
                        val min = progress.size.toFloat()
                        val max = lessons.size.toFloat()
                        val progressPercentage = if (max > 0) min / max else 0f
                        CircularProgressIndicator(
                            progress = {
                                progressPercentage
                            },
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp,
                        )
                    }
                },
                modifier = Modifier
                    .clickable {
                        if (enabled) {
                            isExpanded = !isExpanded
                        }
                    }

            )

            // Show the list of lessons only if expanded
            if (isExpanded) {
                lessons.forEach {
                    LessonCard(
                        isFinished = finished.contains(it.id),
                        lesson = it
                    ) {
                        onClick(it)
                    }
                }
            }
        }
    }
}



@Composable
fun LessonCard(modifier: Modifier =Modifier,isFinished : Boolean ,lesson: SignLanguageLesson ,onClick : () -> Unit) {
    ListItem(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        headlineContent = {
            Text(text = lesson.title!!, style = MaterialTheme.typography.titleMedium.copy(
                color = if (isFinished) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            ))
        },
        supportingContent = {
            Text(text = lesson.desc!!, style = MaterialTheme.typography.labelMedium)
        }
    )
}


