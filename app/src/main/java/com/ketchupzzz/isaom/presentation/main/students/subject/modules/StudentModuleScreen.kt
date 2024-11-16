package com.ketchupzzz.isaom.presentation.main.students.subject.modules

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.presentation.main.students.subject.components.StudentModuleCard
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectEvent
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectState
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError

@Composable
fun StudentModuleScreen(
    modifier: Modifier = Modifier,
    state: StudentViewSubjectState,
    subjects: Subjects,
    events: (StudentViewSubjectEvent) -> Unit,
    navHostController: NavHostController,
) {

    LaunchedEffect(subjects) {
            events.invoke(StudentViewSubjectEvent.OnGetSubjectModules(state.subject?.id!!))
    }
    when {
        state.isGettingModules -> ProgressBar(title = "Getting all Modules")
        state.modules.isEmpty() -> UnknownError(title = "No Activities yet!")
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                items(state.modules, key = {it.id!!}) {module ->
                    StudentModuleCard(modules = module) {
                        if (module.open) {
                            navHostController.navigate(AppRouter.StudentViewModule.createRoute(module))
                        }
                    }
                }
            }
        }
    }
}