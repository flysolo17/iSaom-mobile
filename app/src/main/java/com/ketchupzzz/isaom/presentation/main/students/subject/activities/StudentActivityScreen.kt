package com.ketchupzzz.isaom.presentation.main.students.subject.activities

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.presentation.main.students.subject.components.StudentActivityCard
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectEvent
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectState
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError
import com.ketchupzzz.isaom.utils.toast

@Composable
fun StudentActivityScreen(
    modifier: Modifier = Modifier,
    state: StudentViewSubjectState,
    subjects: Subjects,
    events : (StudentViewSubjectEvent) -> Unit,
    navHostController: NavHostController,
    ) {
    val context = LocalContext.current
    LaunchedEffect(subjects) {
        events.invoke(StudentViewSubjectEvent.OnGetSubjectActivities(state.subject?.id?: ""))
    }

    when {
        state.isGettingActivities -> ProgressBar(title = "Getting All Activities!")
        state.activities.isEmpty() -> UnknownError(title = "No Activities yet!")
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                items(state.activities, key = {it.id!!}) {activity ->
                    StudentActivityCard(activity = activity) {
                        val hasSubmission = state.submissions.any { it.activityID == activity.id }
                        if (hasSubmission){
                            context.toast("You've already has submission")
                            return@StudentActivityCard
                        }
                        if (activity.open) {
                            navHostController.navigate(AppRouter.StudentViewActivity.createRoute(activity))
                        }
                    }
                }
            }
        }
    }
}