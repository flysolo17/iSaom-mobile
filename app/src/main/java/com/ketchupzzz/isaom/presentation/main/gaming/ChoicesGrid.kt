package com.ketchupzzz.isaom.presentation.main.gaming

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ketchupzzz.isaom.ui.custom.HexagonShape
import com.ketchupzzz.isaom.ui.theme.ISaomTheme


@Preview(
    showBackground = true
)
@Composable
private fun SampleGridPrev() {
    ISaomTheme {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ChoicesGrid(
                text = "Permission".trim().uppercase(),
                selectedIndexes = listOf(0,3,5,6),
                onClick = {s,i->

                }
            )
        }

    }
}



@Composable
fun ChoicesGrid(
    modifier: Modifier = Modifier,
    text: String,
    selectedIndexes : List<Int>,
    onClick : (String,Int) -> Unit
) {
    // Calculate columns and rows dynamically based on text length
    val columns = kotlin.math.ceil(kotlin.math.sqrt(text.length.toDouble())).toInt()
    val rows = (text.length + columns - 1) / columns

    Column(
        modifier = modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(-9.dp) ,
    ) {
        for (i in 0 until rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = modifier
                    .wrapContentSize()
                    .padding(start = if (i % 2 == 0)
                        0.dp
                    else
                        30.dp
                    )
            ) {
                for (j in 0 until columns) {
                    val index = i * columns + j
                    if (index < text.length) {
                        val isSelected = selectedIndexes.contains(index)
                        HexagonItem(text[index].toString().uppercase(),isSelected , click = {
                            if (!selectedIndexes.contains(index)) {
                                onClick(text[index].toString(),index)
                            }

                        })
                    }
                }
            }
        }
    }
}



@Composable
fun HexagonItem(text: String , isSelected : Boolean,click : () -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(HexagonShape())
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFFFA500))
            .padding(8.dp)
            .clickable {
                click()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ))

    }
}