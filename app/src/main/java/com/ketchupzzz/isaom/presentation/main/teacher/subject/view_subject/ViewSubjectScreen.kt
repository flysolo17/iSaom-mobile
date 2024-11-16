package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components.FullScreenLoading
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components.FullScreenNoSubject
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components.SubjectHeader
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components.SubjectTabLayout
import com.ketchupzzz.isaom.ui.theme.ISaomTheme
import kotlinx.coroutines.delay

@OptIn( ExperimentalFoundationApi::class)
@Composable
fun ViewSubjectScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    state: ViewSubjectState,
    events: (ViewSubjectEvents) -> Unit,
    subjectID: String
) {
    LaunchedEffect(subjectID) {
        events(ViewSubjectEvents.OnGetSubjectByID(subjectID))
    }

    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.errors !== null) {
            Toast.makeText(context,state.errors,Toast.LENGTH_SHORT).show()
        }
        if (state.deletionSuccess !== null) {
            Toast.makeText(context,state.deletionSuccess,Toast.LENGTH_SHORT).show()
            delay(1000)
            navHostController.popBackStack()
        }
    }
    val modules = state.modules
    val activities = state.activities
    val subjects = state.subjects
    when {
        state.isLoading -> {
            FullScreenLoading(modifier)
        }
        subjects == null -> {
            FullScreenNoSubject(modifier, navHostController)
        }

        state.isDeleting -> DeletingSubject()
        else -> {
            val pagerState = rememberPagerState(pageCount = {3})
            val scope = rememberCoroutineScope()

            Column(modifier = modifier) {
                SubjectHeader(subjects = state.subjects, navHostController = navHostController, events = events, state = state)
                SubjectTabLayout(
                    state = state,
                    events = events,
                    pageState = pagerState,
                    scope = scope,
                    navHostController = navHostController
                )
            }

        }
    }
}

@Composable
fun DeletingSubject() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Deleting subject...")
        }
}

@Preview
@Composable
private fun ViewSubjectScreenPrev() {
    ISaomTheme {
        ViewSubjectScreen(navHostController = rememberNavController(), state = ViewSubjectState(), events = {}, subjectID = "test")
    }

}