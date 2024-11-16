package com.ketchupzzz.isaom.presentation.main.students.subject.view_subject

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.presentation.main.students.subject.activities.StudentActivityScreen
import com.ketchupzzz.isaom.presentation.main.students.subject.modules.StudentModuleScreen
import com.ketchupzzz.isaom.presentation.main.students.subject.submissions.StudentSubmissionsScreen
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentViewSubjectScreen(
    modifier: Modifier = Modifier,
    subjectID : String,
    state : StudentViewSubjectState,
    event: (StudentViewSubjectEvent) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(
        subjectID
    ) {
        if (subjectID.isNotEmpty()) {
            event.invoke(StudentViewSubjectEvent.OnGetSubject(subjectID))
        }
    }


    val pageState = rememberPagerState(0) { 3}
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize()

            .padding(8.dp)
    ) {
        when {
            state.isLoading -> ProgressBar(
                title = "Getting Subject data..."
            )
            state.errors != null && state.subject == null -> UnknownError(
                title = "${state.errors}"
            ) {
                Button(onClick = { navHostController.popBackStack() }) {
                    Text(text = "Back")
                }
            } else -> {
            val subjects = state.subject
            if (subjects != null) {
                TabRow(selectedTabIndex = pageState.currentPage) {
                    SubjectTabs(title = "Modules", isSelected = pageState.currentPage == 0) {
                        scope.launch {
                            pageState.animateScrollToPage(0)
                        }
                    }
                    SubjectTabs(title = "Activities", isSelected = pageState.currentPage == 1) {
                        scope.launch {
                            pageState.animateScrollToPage(1)
                        }
                    }

                    SubjectTabs(title = "Submissions", isSelected = pageState.currentPage == 2) {
                        scope.launch {
                            pageState.animateScrollToPage(2)
                        }
                    }
                }
                HorizontalPager(state = pageState) {
                    when (it) {
                        0 -> {
                            StudentModuleScreen(
                                state = state,
                                events = event,
                                subjects = subjects,
                                navHostController = navHostController
                            )
                        }
                        1 -> {
                            StudentActivityScreen(
                                state = state,
                                subjects = subjects,
                                events = event,
                                navHostController = navHostController
                            )
                        }
                        2 -> {
                            StudentSubmissionsScreen(
                                state = state,
                                events = event,
                                subjects = subjects,
                                navHostController = navHostController
                            )
                        }
                        else -> {
                            UnknownError(
                                title = "Unknown Error"
                            ) {
                                Button(onClick = { navHostController.popBackStack() }) { Text(text = "Back") }
                            }
                        }
                    }
                }
            }
        }
        }


    }
}


@Composable
fun SubjectTabs(
    title: String,
    isSelected: Boolean,
    onSelectTab: () -> Job
) {
    Tab(
        selected = isSelected,
        text = {
            Text(text = title)
        },
        onClick = { onSelectTab() }
    )
}