package com.ketchupzzz.isaom.presentation.main.teacher.sections.view_section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.presentation.main.translator.shareText
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.ui.custom.SubjectCard
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError


@Composable
fun ViewSectionScreen(
    modifier: Modifier = Modifier,
    sectionID : String,
    state: ViewSectionState,
    events: (ViewSectionEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(sectionID) {
        if (sectionID.isNotEmpty()) {
            events(ViewSectionEvents.getSectionWithSubjects(sectionID))
        }
    }

    when {
        state.isLoading -> {
            ProgressBar(
                title = "getting section data.."
            )
        }
        !state.isLoading && state.sections == null && state.errors != null -> UnknownError(
            title = "No section Found!"
        )
        else -> {
            MainViewSectionScreen(
                modifier = modifier,
                state = state,
                events = events,
                navHostController= navHostController,

            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainViewSectionScreen(
    modifier: Modifier = Modifier,
    state: ViewSectionState,
    events: (ViewSectionEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
    ) {
        items(state.subjects) { subject->
            SubjectCard(subject = subject , onClick = {
                navHostController.navigate(AppRouter.ViewSubject.createRoute(subjectID = subject.id ?:""))
            }) {
                Row(
                    modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${subject.code}", style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White
                    ))
                    IconButton(onClick = { shareText(context = context, text = subject.code ?:"" ) }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                }
            }
        }
    }
}
