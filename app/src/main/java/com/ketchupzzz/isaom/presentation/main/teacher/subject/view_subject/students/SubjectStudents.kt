package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.students

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectState
import com.ketchupzzz.isaom.utils.AvatarPhoto


@Composable
fun SubjectStudents(
    modifier: Modifier = Modifier,
    state : ViewSubjectState
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.students) {
            Studentcard(student = it , onRemove = {})
        }
    }
}

@Composable
fun Studentcard(
    modifier: Modifier = Modifier,
    student : Users,
    onRemove : () -> Unit
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AvatarPhoto(imageUrl = student.avatar ?: "", size = 32.dp)
            Text(text = "${student.name}", style = MaterialTheme.typography.titleLarge)
//            FilledIconButton(
//                colors = IconButtonDefaults.iconButtonColors(
//                    containerColor = MaterialTheme.colorScheme.errorContainer,
//                    contentColor = MaterialTheme.colorScheme.onErrorContainer
//                ),
//                onClick = { onRemove() }
//            ) {
//                Icon(imageVector = Icons.Rounded.Remove, contentDescription = "Remove")
//            }
        }
    }
}
