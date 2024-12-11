package com.ketchupzzz.isaom.presentation.main.students.subject.activities.view

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.widget.Space
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity.QuestionCard
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError
import com.ketchupzzz.isaom.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun StudentViewActivity(
    modifier: Modifier = Modifier,
    activity: Activity,
    state: StudentViewActivityState,
    events: (StudentViewActivityEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current


    LaunchedEffect(
        activity
    ) {
        if (!activity.id.isNullOrEmpty()) {
            events.invoke(StudentViewActivityEvents.OnGetActivityQuestions(activity.id))
        }
    }

    var showFinishDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.isSubmitted != null) {
            context.toast(state.isSubmitted)
            showFinishDialog = true

        }
    }
    if (showFinishDialog) {
        var points = 0
        var maxPoints = 0

        state.questions.forEach {
            maxPoints += it.points
            val answer = state.answers[it.id]
            if (answer == it.answer) {
                points += it.points
            }
        }
        FinishActivityDialog(
            score = points,
            maxScore = maxPoints
        ) {      showFinishDialog = !showFinishDialog
            navHostController.popBackStack()
        }
    }

    when {
        state.isLoading -> ProgressBar(title = "Getting all contents")
        state.errors != null-> UnknownError(title =state.errors) {
            Button(onClick = {navHostController.popBackStack()}) {
                Text(text = "Back")
            }
        }
        state.questions.isEmpty() -> UnknownError(title = "No questions yet!")
        else -> {
            TakeActivityScreen(
                activity = activity,
                state = state,
                events = events
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TakeActivityScreen(
    modifier: Modifier = Modifier,
    state: StudentViewActivityState,
    events: (StudentViewActivityEvents) -> Unit,
    activity: Activity
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0
    ) { state.questions.size }


    val currentQuestion = state.questions[pagerState.currentPage]
    val context  = LocalContext.current
    var classificationResult by remember { mutableStateOf(
        state.answers[currentQuestion.id] ?: "Press 'Classify Audio' to start"
    ) }

    LaunchedEffect(pagerState.currentPage) {
        classificationResult  = state.answers[currentQuestion.id] ?: "Press 'Classify Audio' to start"
    }
    var isProcessing by remember { mutableStateOf(false) }

    val audioClassifierHelper = remember {
        AudioClassifierHelper(
            context = context,
            listener = object : AudioClassificationListener {
                override fun onError(error: String) {
                    classificationResult = "Error: $error"
                    isProcessing = false
                }
                override fun onResult(results: List<Category>, inferenceTime: Long) {
                    val detectedLabels = results.joinToString(", ") { it.label.replace(Regex("\\d+"), "").trim() }
                    classificationResult = detectedLabels
                    isProcessing = false
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            audioClassifierHelper.stopAudioClassification()
        }
    }
    LaunchedEffect(classificationResult) {
        if (classificationResult != "Background Noise") {
            if (classificationResult.contains(currentQuestion.answer!!)) {
                classificationResult = currentQuestion.answer!!
                events(StudentViewActivityEvents.OnUpdateAnswers(currentQuestion.id ?: "", currentQuestion.answer))
                audioClassifierHelper.stopAudioClassification()
            }
        }
    }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = activity.title.toString().take(10),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        val current = state.answers.size
        val max = state.questions.size

        LinearProgressIndicator(
            progress = {
                if (max > 0) current / max.toFloat() else 0f
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        HorizontalPager(
            pagerState,
            userScrollEnabled = false,
            modifier = modifier.weight(1f)
        ) {
            val question = state.questions[it]
            QuestionDisplay(
                modifier = modifier,
                currentAnswer = state.answers[question.id],
                question = question,
                onSelectAnswer = {
                    events(
                        StudentViewActivityEvents.OnUpdateAnswers(question.id ?: "", it)
                    )
                },
                onStartClassification = {
                    audioClassifierHelper.startAudioClassification()
                },
                onStopClassification = {
                    audioClassifierHelper.stopAudioClassification()
                },
                isProcessing = isProcessing,
                classificationResult = classificationResult
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pagerState.currentPage > 0) {
                TextButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage - 1
                            )
                        }
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                        Text("Back")
                    }

                }
            } else {
                Box {
                    Text("")
                }
            }

            val isSubmit = pagerState.currentPage  == state.questions.size - 1
            Button(
                onClick = {
                    if (isSubmit) {
                        events.invoke(StudentViewActivityEvents.OnSubmitAnswer(activity = activity))
                    } else {
                        scope.launch {

                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                shape = MaterialTheme.shapes.small
            ) { Text(
                text = if (isSubmit)"Submit" else "next"
            ) }
        }
    }
}

@Composable
fun QuestionDisplay(
    modifier: Modifier = Modifier,
    question: Question,
    currentAnswer : String ?,
    onSelectAnswer : (String) -> Unit,
    isProcessing: Boolean,
    classificationResult: String,
    onStartClassification: () -> Unit,
    onStopClassification : () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("${question.title}", style = MaterialTheme.typography.labelLarge.copy(
                    textAlign = TextAlign.Center
                ))
                if (question.image != null) {
                    if (!question.image.isNullOrEmpty()) {
                        AsyncImage(
                            model = question.image,
                            contentDescription = "${question.title} cover",
                            modifier = Modifier
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = modifier.weight(1f)
        )
        if (question.choices.isNotEmpty()) {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                question.choices.forEach { choice ->
                    val isSelect = currentAnswer == choice
                    if (isSelect) {
                        Button(
                            modifier = modifier.fillMaxWidth(),
                            onClick = {
                                onSelectAnswer(choice)
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Box(
                                modifier = modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = choice,
                                    modifier = modifier.padding(8.dp)
                                )
                            }

                        }
                    } else {
                        OutlinedButton(
                            modifier = modifier.fillMaxWidth(),
                            onClick = {
                                onSelectAnswer(choice)
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Box(
                                modifier = modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = choice,
                                    modifier = modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {

            AudioInput(
                isProcessing = isProcessing,
                classificationResult = classificationResult,
                onStartClassification = {
                    onStartClassification()
                },
                onStopClassification = {
                    onStopClassification()
                },
                isCorrect = classificationResult.contains(question.answer!!)
            )
        }

    }
}




@Composable
fun AudioInput(
    modifier: Modifier = Modifier,
    isProcessing: Boolean,
    classificationResult: String,
    onStartClassification: () -> Unit,
    onStopClassification: () -> Unit,
    isCorrect : Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledIconButton(
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isProcessing) Color.White else MaterialTheme.colorScheme.primary,
                contentColor = if (isProcessing) Color.Black else MaterialTheme.colorScheme.onPrimary
            ),
            onClick = {
                if (isProcessing) {
                    onStopClassification()
                } else {
                    onStartClassification()
                }
            }
        ) {
            Icon(
                imageVector =  Icons.Default.Mic,
                contentDescription = if (isProcessing) "Stop" else "Start"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = classificationResult.uppercase(),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = if (isCorrect) {
                    Color(0xFF388E3C)
                } else {
                    Color(0xFFD32F2F)
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
