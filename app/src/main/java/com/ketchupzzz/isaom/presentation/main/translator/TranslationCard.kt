package com.ketchupzzz.isaom.presentation.main.translator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.history.TranslatorHistory
import com.ketchupzzz.isaom.presentation.main.dictionary.DictionaryEvents
import com.ketchupzzz.isaom.presentation.main.dictionary.words.openLink


@Composable
fun TranslationCard(
    modifier: Modifier = Modifier,
    history: TranslatorHistory,
    onCopy : () -> Unit,
    onShare : () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(text = history.source?.name ?: "No name", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
            Text(text = history.text ?: "No Word" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = modifier.height(2.dp))
            HorizontalDivider()
            Spacer(modifier = modifier.height(2.dp))
            Text(text = history.target?.name ?: "No name" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
            Text(text = history.translation ?: "No Definition" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = modifier
                    .align(Alignment.End)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { onCopy() }) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy")
                }
                IconButton(onClick = { onShare() }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    }
}