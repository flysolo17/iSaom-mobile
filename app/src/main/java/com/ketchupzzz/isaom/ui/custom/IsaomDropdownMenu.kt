package com.ketchupzzz.isaom.ui.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ketchupzzz.isaom.models.SourceAndTargets



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsaomDropdownMenu(
    modifier: Modifier = Modifier,
    selectedValue: SourceAndTargets,
    options: List<SourceAndTargets> = SourceAndTargets.entries,
    onValueChangedEvent: (SourceAndTargets) -> Unit,

) {
    var expanded by remember { mutableStateOf(false) }
    val textFielfColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent
    )
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            textStyle = MaterialTheme.typography.labelSmall,
            value = selectedValue.name,
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = textFielfColors,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option: SourceAndTargets ->
                DropdownMenuItem(
                    text = { Text(text = option.name) },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(option)
                    }
                )
            }
        }
    }
}