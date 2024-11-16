package com.ketchupzzz.isaom.presentation.main.students.subject.submissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.models.submissions.Submissions
import com.ketchupzzz.isaom.models.submissions.toIsaomFormat
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectEvent
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectState
import kotlinx.coroutines.delay


@Composable
fun StudentSubmissionsScreen(
    modifier: Modifier = Modifier,
    state: StudentViewSubjectState,
    subjects: Subjects,
    events: (StudentViewSubjectEvent) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(
        subjects
    ) {
        delay(1000)
        events(StudentViewSubjectEvent.OnGetSubmissions)
    }
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(state.submissions) {
            SubmissionCard(it = it)
        }
    }
}

@Composable
fun SubmissionCard(
    modifier: Modifier = Modifier,
    it: Submissions) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
               
            ) {
                Text(
                    modifier = modifier.weight(1f),
                    text = "${it.activityName}", 
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${it.points} / ${it.maxPoints}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${it.createdAt.toIsaomFormat()}",
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
