package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity.components

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ketchupzzz.isaom.models.submissions.AnswersByQuestion
import com.ketchupzzz.isaom.utils.generateRandomColors
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionsCardPie(
    modifier: Modifier = Modifier,
    it: AnswersByQuestion
) {
    val randomColors = generateRandomColors(it.toPieChartData().size)
    Log.d("test",it.toString())
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val data = it.toPieChartData()
        Column(
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = it.question.title.toString(),
                style = MaterialTheme.typography.titleLarge
            )
            FlowRow(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                data.forEachIndexed { index, pieChartSlice ->
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = randomColors[index],
                            contentColor = Color.White
                        )
                    ) {
                        Box(
                            modifier = modifier.padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${pieChartSlice.label.uppercase()} = ${pieChartSlice.value}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }
            }
            Row(
                modifier = modifier.fillMaxWidth(),
            ) {
                PieChart(
                    modifier = modifier
                        .size(150.dp)
                        .weight(1f),
                    data = data.mapIndexed { index, pieData ->
                        Pie(
                            label = pieData.label,
                            data = pieData.value.toDouble(),
                            color = randomColors[index],
                            selectedColor = Color.Gray
                        )
                    },
                    selectedScale = 1.2f,
                    scaleAnimEnterSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    colorAnimEnterSpec = tween(300),
                    colorAnimExitSpec = tween(300),
                    scaleAnimExitSpec = tween(300),
                    spaceDegreeAnimExitSpec = tween(300),
                    style = Pie.Style.Fill
                )
                Column(
                    modifier = modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        )
                    ) {
                        Box(
                            modifier = modifier.padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Correct = ${it.correctAnswers}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Box(
                            modifier = modifier.padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Wrong = ${it.wrongAnswers}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }

                }
            }

        }
    }
}
