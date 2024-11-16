package com.ketchupzzz.isaom.presentation.main.students.subject.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.ketchupzzz.isaom.models.subject.module.Modules

@Composable
fun StudentModuleCard(
    modifier: Modifier = Modifier,
    modules: Modules,
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
                Text(text = "${modules.title}", style = MaterialTheme.typography.titleMedium)
                Text(text = "${modules.desc}", style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                imageVector = if (modules.open) Icons.Default.LockOpen  else Icons.Default.Lock,
                contentDescription = "Lock"
            )
        }
    }
}