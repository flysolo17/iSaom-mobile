package com.ketchupzzz.isaom.presentation.main.students.subject.view_subject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.subject.Subjects


@Composable
fun StudentSubjectheader(
    modifier: Modifier = Modifier,
    subjects : Subjects,
    state: StudentViewSubjectState,
    event: (StudentViewSubjectEvent) -> Unit,
    onBackPress : () -> Unit
) {

    Box(
        modifier = modifier
            .height(200.dp)
    ) {
        AsyncImage(
            model = subjects.cover,
            contentDescription = "${subjects.name} cover",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.placeholder),
            error = painterResource(id = R.drawable.placeholder),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val color = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = {onBackPress() }, colors = color) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back",)
                }

            }
            Text(text = subjects.name ?: "", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }

    }

}