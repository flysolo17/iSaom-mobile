package com.ketchupzzz.isaom.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imageURL: String,
    size: Dp = 42.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        if (imageURL.isEmpty()) {
            Image(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Profile",
                modifier = Modifier.fillMaxSize().background(
                    color = MaterialTheme.colorScheme.primary
                ),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = imageURL,
                contentDescription = "Profile",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

