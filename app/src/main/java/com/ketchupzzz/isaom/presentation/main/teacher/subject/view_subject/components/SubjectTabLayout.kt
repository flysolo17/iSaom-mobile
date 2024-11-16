package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectEvents
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectState

import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.ActivityPage
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.ModulePage
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.students.SubjectStudents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubjectTabLayout(
    state : ViewSubjectState,
    events: (ViewSubjectEvents) -> Unit,
    pageState : PagerState,
    scope : CoroutineScope,
    navHostController: NavHostController
) {
    TabRow(selectedTabIndex = pageState.currentPage) {
        Tab(
            selected = pageState.currentPage == 0,
            text = {
                Text(text = "Modules")
            },
            onClick = {
                scope.launch {
                    pageState.animateScrollToPage(0)
                }
            })
        Tab(
            selected = pageState.currentPage == 1,
            text = {
                Text(text = "Activities")
            },
            onClick = {
                scope.launch {
                    pageState.animateScrollToPage(1)
                }
            })

        Tab(
            selected = pageState.currentPage == 2,
            text = {
                Text(text = "Students")
            },
            onClick = {
                scope.launch {
                    pageState.animateScrollToPage(2)
                }
            })
    }
    HorizontalPager(state = pageState) {
        when(it) {
            0 -> ModulePage(
                state = state,
                events =events,
                navHostController = navHostController)
            1 -> ActivityPage(
                state = state,
                events =events,
                navHostController = navHostController
            )
            2 -> SubjectStudents(state = state)
            else -> FullScreenNoSubject(navHostController = navHostController)
        }
    }
}



