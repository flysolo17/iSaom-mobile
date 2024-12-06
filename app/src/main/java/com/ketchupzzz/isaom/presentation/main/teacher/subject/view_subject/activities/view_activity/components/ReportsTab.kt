package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ketchupzzz.isaom.models.subject.StudentWithSubmissions
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.models.submissions.SubmissionWithStudent
import com.ketchupzzz.isaom.models.submissions.Submissions
import com.ketchupzzz.isaom.models.submissions.getResponses
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import kotlin.math.roundToInt


@Composable
fun ReportsTab(
    modifier: Modifier = Modifier,
    questions : List<Question>,
    submissions : List<SubmissionWithStudent>
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(questions) {
            QuestionReportCard(
                question = it,
                submissions  = submissions.map { it.submissions!!}
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionReportCard(
    modifier: Modifier = Modifier,
    question: Question,
    submissions: List<Submissions>
) {
    val data = question.getResponses(submissions)
    val totalResponses = data.sumOf { it.totalCount }

    val colors = generateColors(data.size)
    val selectedColors = generateColors(data.size).map { it.copy(alpha = 0.8f) }

    var pie by remember {
        mutableStateOf(
            data.mapIndexed { index, response ->
                Pie(
                    label = response.choice,
                    data = response.totalCount.toDouble(),
                    color = colors[index],
                    selectedColor = selectedColors[index]
                )
            }
        )
    }
    OutlinedCard(
        modifier = modifier.fillMaxSize().padding(8.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(
                text = question.title?: "unknown",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (totalResponses > 0) {
                PieChart(
                    modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally),
                    data = pie,
                    onPieClick = {
                        println("${it.label} Clicked")
                        val pieIndex = pie.indexOf(it)
                        pie = pie.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
                    },
                    selectedScale = 1.2f,
                    scaleAnimEnterSpec = spring<Float>(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    colorAnimEnterSpec = tween(300),
                    colorAnimExitSpec = tween(300),
                    scaleAnimExitSpec = tween(300),
                    spaceDegreeAnimExitSpec = tween(300),
                    style = Pie.Style.Fill
                )

                Spacer(modifier = Modifier.height(8.dp))


                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.forEachIndexed { index, response ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(color = colors[index], shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${response.choice}: ${response.totalCount} (${(response.totalCount.toFloat() / totalResponses * 100).roundToInt()}%)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No submissions yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}



fun generateColors(count: Int): List<Color> {
    val predefinedColors = listOf(
        Color(0xFFF4B400), // Yellow-Orange
        Color(0xFF0F9D58), // Green
        Color(0xFF4285F4), // Blue
        Color(0xFFDB4437), // Red
        Color(0xFFAB47BC),
        Color(0xFF00ACC1), // Cyan
        Color(0xFFFF7043), // Deep Orange
        Color(0xFFFFA000)  // Amber
    )

    // Repeat colors if count exceeds predefined colors
    return List(count) { index ->
        predefinedColors[index % predefinedColors.size]
    }
}
