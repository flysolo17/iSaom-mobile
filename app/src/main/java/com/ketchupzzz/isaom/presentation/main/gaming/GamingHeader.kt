package com.ketchupzzz.isaom.presentation.main.gaming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GamingHeader(
    modifier: Modifier = Modifier,
    state: GamingState,
    level : String,
    hint : String,
    isPlaying : Boolean,
    onChangeMusicStatus : (Boolean) -> Unit
) {
    val minutes = state.timer / 60
    val seconds = state.timer % 60
    val clock = String.format("%02d:%02d", minutes, seconds)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            level,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = clock,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            ShowHint(hint = hint)

            IconButton(onClick = {
                onChangeMusicStatus(!isPlaying)
            }) {
               Icon(
                    imageVector = if (isPlaying)  Icons.Filled.VolumeUp else Icons.Filled.VolumeMute,
                    "Audio"
                )
            }
        }



    }
}
