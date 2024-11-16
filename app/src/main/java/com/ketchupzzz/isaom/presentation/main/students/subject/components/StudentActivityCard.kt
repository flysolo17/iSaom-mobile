package com.ketchupzzz.isaom.presentation.main.students.subject.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ketchupzzz.isaom.models.subject.activities.Activity


@Composable
fun StudentActivityCard(
    modifier: Modifier = Modifier,
    activity: Activity,
    onClick : () -> Unit
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(4.dp)
        .clickable {
            onClick()
        }
    ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = modifier
                .fillMaxSize()
                .weight(1f)) {
                Text(text = "${activity.title}", style = MaterialTheme.typography.titleMedium)
                Text(text = "${activity.desc}", style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                imageVector = if (activity.open) Icons.Default.LockOpen  else Icons.Default.Lock,
                contentDescription = "Delete"
            )
        }
    }
}