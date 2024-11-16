package com.ketchupzzz.isaom.presentation.main.students.subject.activities.view

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError
import com.ketchupzzz.isaom.utils.indexToLetter
import com.ketchupzzz.isaom.utils.toast
import kotlinx.coroutines.delay


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudentQuestionCard(
    modifier: Modifier = Modifier,
    question: Question,
    currentAnswer: String?,
    hasAnswer: Boolean,
    onSelectAnswer: (String) -> Unit
) {
    Card(
        border = BorderStroke(
            width = 1.dp,
            color = if (hasAnswer) Color.Green else Color.Red
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = question.title ?: "", style = MaterialTheme.typography.titleMedium)
                Text(text = question.points.toString(), style = MaterialTheme.typography.titleMedium)
            }
            Text(text = question.desc ?: "", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = modifier.height(8.dp))
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
            Spacer(modifier = modifier.height(8.dp))
            FlowRow(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                question.choices.forEach {
                    val isAnswer = it == currentAnswer
                    Card(
                        modifier = modifier.clickable {
                             onSelectAnswer(it)
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isAnswer) Color.Green else MaterialTheme.colorScheme.background,
                            contentColor = if (isAnswer) Color.White else MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Box(
                            modifier = modifier.wrapContentWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = it,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


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
            LazyColumn(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                item {
                    Column(
                        modifier = modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = activity.title ?: "",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = activity.desc ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                items(state.questions) {
                    val hasAnswer = state.answers.containsKey(it.id)
                    StudentQuestionCard(
                        question = it,
                        hasAnswer = hasAnswer,
                        currentAnswer = state.answers[it.id],
                        onSelectAnswer =  { answer : String ->
                            events(
                                StudentViewActivityEvents.OnUpdateAnswers(
                                    it.id ?: "",
                                    answer)
                            )
                        }
                    )
                }
                item {
                    Button(
                        modifier = modifier
                            .padding(8.dp),
                        onClick = {
                            events.invoke(StudentViewActivityEvents.OnSubmitAnswer(activity = activity))
                        }
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}

@Composable
fun Choices(
    modifier: Modifier = Modifier,
    choices : String,
    index : Int,
    isCorrect : Boolean,
    selectAnswer : (ans : String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isCorrect) Color.Green else MaterialTheme.colorScheme.surface
            )
            .clickable {
                selectAnswer(choices)
            }
            .padding(4.dp)
    ) {
        Text(
            text = "${index.indexToLetter()}. ${choices}",
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(8.dp)
        )
    }
}
