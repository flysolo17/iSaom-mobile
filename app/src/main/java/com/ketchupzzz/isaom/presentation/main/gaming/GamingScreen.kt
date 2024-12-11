package com.ketchupzzz.isaom.presentation.main.gaming

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import android.util.Log.e
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.games.GameSubmission
import com.ketchupzzz.isaom.models.games.Games
import com.ketchupzzz.isaom.models.games.Levels
import com.ketchupzzz.isaom.models.games.geMaxScore
import com.ketchupzzz.isaom.models.games.getScore
import com.ketchupzzz.isaom.ui.custom.HexagonShape
import com.ketchupzzz.isaom.ui.theme.ISaomTheme
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError
import com.ketchupzzz.isaom.utils.generateRandomString
import com.ketchupzzz.isaom.utils.getSpacesInString
import com.ketchupzzz.isaom.utils.shuffleString
import com.ketchupzzz.isaom.utils.toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GamingScreen(
    modifier: Modifier = Modifier,
    games: Games ,
    state: GamingState,
    events : (GamingEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var mediaPlayer: MediaPlayer? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            mediaPlayer?.start()
        } else {
            mediaPlayer?.pause()
        }
    }

    DisposableEffect(context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.music).apply {
            setOnCompletionListener {
                this.seekTo(0)
                this.start()
            }
        }
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
    LaunchedEffect(isPlaying) {
        Log.d("MusicPlayer", " isPlaying: $isPlaying")
        if (isPlaying) {
            Log.d("MusicPlayer", "Starting music...")
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
        } else {
            Log.d("MusicPlayer", "Pausing music...")
            mediaPlayer?.pause()
        }
    }


    var dialog by remember {
        mutableStateOf(false)
    }


    var congratsDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(games.id) {
        if (!games.id.isNullOrEmpty()) {
            events(GamingEvents.OnGetLevels(games))
        }
    }
    LaunchedEffect(state) {
        if (state.submitted != null) {
            context.toast(state.submitted)
        }
    }
    LaunchedEffect(state.timerFinish) {
        if (state.timerFinish) {
            dialog = true
        }
    }

    if (dialog) {
        FinishDialog(
            score =state.levels.getScore(state.answerSheet),
            maxScore = state.levels.geMaxScore()
        ) {

            dialog= !dialog
            val gameSubmission = GameSubmission(
                id = generateRandomString(),
                gameID = games.id,
                userID = state.users?.id,
                score = state.levels.getScore(state.answerSheet),
                answerSheet = state.answerSheet,
                maxScore = state.levels.geMaxScore(),
                createdAt = Date()
            )
            events.invoke(GamingEvents.OnFinish(gameSubmission,navHostController))
        }
    }
    if (congratsDialog) {
        CongratsDialog(
            score =state.levels.getScore(state.answerSheet),
            maxScore = state.levels.geMaxScore()
        ) {
            congratsDialog= !congratsDialog
            val gameSubmission = GameSubmission(
                id = generateRandomString(),
                gameID = games.id,
                userID = state.users?.id,
                score = state.levels.getScore(state.answerSheet),
                answerSheet = state.answerSheet,
                maxScore = state.levels.geMaxScore(),
                createdAt = Date()
            )
            events.invoke(GamingEvents.OnFinish(gameSubmission,navHostController))
        }
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            state.isSubmitting -> ProgressBar(
                title = "Submitting score..."
            )
            state.isLoading -> ProgressBar(
                title = "Getting levels"
            )
            state.errors != null -> UnknownError(
                title = state.errors
            ) {
                TextButton(onClick = {navHostController.popBackStack()}) {
                    Text("Back")
                }
            } else -> {
            if (state.levels.isEmpty()) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text("No Levels yet") }
            } else {
                val pageState = rememberPagerState(initialPage = 0, pageCount = { state.levels.size })
                val scope = rememberCoroutineScope()

                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    GamingHeader(
                        modifier = modifier,
                        state = state,
                        level = "Level ${pageState.currentPage + 1} / ${state.levels.size}",
                        hint = state.levels[pageState.currentPage].hint ?: "",
                        isPlaying = isPlaying,
                        onChangeMusicStatus = { status ->
                            isPlaying = status

                        }
                    )
                    HorizontalPager(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .verticalScroll(rememberScrollState()),
                        state =  pageState,
                        userScrollEnabled = false
                    ) { i ->
                        val level = state.levels[i]
                        LevelScreen(
                            level =level,
                            state = state,
                            events = events,
                            onNext = { ans ->
                                events(GamingEvents.OnAddAnser(
                                    ans
                                ))
                                scope.launch {
                                    if (i < state.levels.size - 1) {
                                        events(GamingEvents.OnReset)
                                        pageState.animateScrollToPage(i + 1)
                                    } else {
                                        congratsDialog = true
                                    }
                                }
                            }
                        )
                    }

                }
            }
            }
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun LevelScreen(
    modifier: Modifier = Modifier,
    level : Levels,
    state: GamingState,
    events: (GamingEvents) -> Unit,
    onNext: (Pair<String,String>) -> Unit
) {
    val shuffledString by remember {
        mutableStateOf(level.answer?.shuffleString() ?: "")
    }
    if (level.answer?.lowercase() == state.answers.lowercase()) {
        val mapAns = (level.id!! to  state.answers)
        onNext(mapAns)
    }
//    if (level.answer?.length === state.answers.length) {
//        events(GamingEvents.OnReset)
//    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("${level.question}", style = MaterialTheme.typography.titleLarge)
        Spacer(
            modifier = modifier.height(8.dp)
        )
        AsyncImage(
            level.image,
            contentScale =ContentScale.Crop,
            error = painterResource(R.drawable.logo),
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(
                    MaterialTheme.shapes.medium
                ),
            contentDescription = level.question
        )
        Spacer(
            modifier = modifier.height(8.dp)
        )
        ChoicesBox(
            text = state.answers,
            answer = level.answer ?: "",
        )
        Spacer(
            modifier = modifier.height(8.dp)
        )
        IconButton(
            onClick = {events(GamingEvents.OnReset)}
        ) {
            Icon(
                imageVector = Icons.Filled.RotateLeft,
                contentDescription = "Reset"
            )
        }
        Spacer(modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ChoicesGrid(
                text = shuffledString,
                selectedIndexes = state.answerIndex,
                onClick = { s,i->
                    var text = s
                    if (level.answer?.getOrNull(state.answers.length) == ' ') {
                        text = " " + s
                    }

                    events(GamingEvents.OnAnswerChanged(text,i))
                }
            )
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChoicesBox(
    modifier: Modifier = Modifier,
    text : String,
    answer : String,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        answer.forEachIndexed { index, c ->
            if (c.toString() == " ") {
                Spacer(modifier = modifier.width(24.dp))
            } else {
                Card(
                    modifier = modifier
                        .size(36.dp)
                        .padding(2.dp)

                ) {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        val textByIndex = text.getOrNull(index)
                        Text(textByIndex?.uppercase() ?: "", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}