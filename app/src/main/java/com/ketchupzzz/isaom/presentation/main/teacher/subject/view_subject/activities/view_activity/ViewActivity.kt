package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.create_question.CreateQuestion
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.edit_question.EditQuestionScreen
import com.ketchupzzz.isaom.utils.AvatarPhoto
import com.ketchupzzz.isaom.utils.UnknownError
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ViewActivityScreen(
    modifier: Modifier = Modifier,
    activityID: String,
    state: ViewActivityState,
    events: (ViewActivityEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(activityID) {
        if (activityID.isNotEmpty()) {
            events.invoke(ViewActivityEvents.OnGetActivityByID(activityID))
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state) {
        state.errors?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
        state.messages?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    Box(modifier = modifier
        .fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
            }
            state.activity == null -> {
                UnknownError(
                    title = "No activity Found!",
                    modifier = modifier.align(Alignment.Center)
                ) {
                    Button(onClick = { navHostController.popBackStack() }) {
                        Text(text = "Back")
                    }
                }
            }
            else -> {
                val pageState = rememberPagerState { 3 }
                val scope = rememberCoroutineScope()
                Column(
                    modifier = modifier.fillMaxSize()
                ) {
                    TabRow(selectedTabIndex = pageState.currentPage) {
                        Tab(
                            selected = pageState.currentPage == 0,
                            text = { Text(text = "Questions") },
                            onClick = {
                                scope.launch {
                                    pageState.animateScrollToPage(0)
                                }
                            }
                        )
                        Tab(
                            selected = pageState.currentPage == 1,
                            text = { Text(text = "Submissions") },
                            onClick = {
                                scope.launch {
                                    pageState.animateScrollToPage(1)
                                }
                            }
                        )

                    }

                    HorizontalPager(state = pageState) { index ->
                        when (index) {
                            0 -> QuestionsTab(activityID = activityID, state = state, events = events)
                            1 -> SubmissionsTab(activityID = activityID, state = state, events = events) // Placeholder for SubmissionsTab
                        }
                    }
                }



            }
        }
    }
}

@Composable
fun QuestionsTab(
    modifier: Modifier = Modifier,
    activityID: String,
    state : ViewActivityState,
    events: (ViewActivityEvents) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(state.questions) {  question ->
            QuestionCard(
                question = question,
                events = events,
                activityID = activityID,
                onDelete = { events(ViewActivityEvents.OnEdeleteQuestion(activityID, question))}
            )
        }
        item {
            CreateQuestion(activityID = activityID, events = events)
        }
    }

}


@Composable
fun SubmissionsTab(
    modifier: Modifier = Modifier,
    activityID: String,
    state : ViewActivityState,
    events: (ViewActivityEvents) -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(state.submissionWithStudent) {
            OutlinedCard(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AvatarPhoto(imageUrl = it.student?.avatar ?: "", size = 32.dp)
                    Text(text = "${it.student?.name}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = modifier.weight(1f)
                    )
                    Text(
                        text = "${it.submissions?.points} / ${it.submissions?.maxPoints}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}




@Composable
fun QuestionCard(
    modifier: Modifier = Modifier,
    question: Question,
    activityID: String,
    events: (ViewActivityEvents) -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .wrapContentHeight()
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
            CustomListBox(
                modifier = modifier,
                title = "Choices",
                actions = question.choices,
                answer = question.answer ?: ""
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(

                    modifier = modifier.weight(1f),
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(text = "Delete", modifier = modifier)
                }
                EditQuestionScreen(activityID = activityID, question = question, events = events, button = modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomListBox(
    modifier: Modifier,
    title: String,
    actions: List<String>,
    answer: String = ""
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .padding(2.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        FlowRow(
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            actions.forEach {
                val isAnswer = it == answer
                Card(
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



@Composable
fun QuestionHeader(
    modifier: Modifier = Modifier,
    index: Int,
    size: Int,
    onDelete : (index : Int) -> Unit,
    onEdit : (index : Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Page ${index + 1} of $size", style = MaterialTheme.typography.titleMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = { onDelete(index) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Settings")
            }
            IconButton(onClick = { onEdit(index) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        }

    }
}


