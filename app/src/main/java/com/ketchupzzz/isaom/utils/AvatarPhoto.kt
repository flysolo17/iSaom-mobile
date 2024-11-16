package com.ketchupzzz.isaom.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.R


@Composable
fun AvatarPhoto(
    imageUrl: String,
    modifier: Modifier = Modifier,
    size : Dp = 32.dp
) {
    AsyncImage(
        model = imageUrl,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp,Color.White, CircleShape),
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.user_filled),
        placeholder = painterResource(id = R.drawable.user_filled),
        contentDescription = "Avatar",
    )
}

@Composable
fun StudentsAvatar(
    avatars: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy((-12).dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        avatars.take(3).forEach { avatar ->
            AvatarPhoto(imageUrl = avatar)
        }
        if (avatars.size > 3) {
            Text(text = "and ${avatars.size - 3} others.")
        }
    }
}
