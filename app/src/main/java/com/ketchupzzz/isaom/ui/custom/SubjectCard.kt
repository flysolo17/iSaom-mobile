package com.ketchupzzz.isaom.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.models.subject.Subjects


@Composable
fun SubjectCard(
    modifier: Modifier = Modifier,
    subject : Subjects,

    onClick : (subjectID : String) -> Unit,
    content : @Composable () -> Unit,
) {
    //add background image and overlay to the box and Text in the bottom Left
    Box(modifier = modifier
        .fillMaxWidth()
        .height(180.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        .clickable { onClick(subject.id ?: "") }

    ) {
        AsyncImage(
            model = subject.cover,
            contentDescription = "${subject.name} cover",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        // Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = modifier.fillMaxWidth().align(Alignment.BottomStart)
        ) {
            Text(
                text = subject.name ?: "No Subject name",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(8.dp)
            )
            content()
        }

    }
}
