package com.ketchupzzz.isaom.presentation.main.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.games.Games
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    state: GameState,
    events: (GameEvents) -> Unit,
    navHostController: NavHostController
) {
val context = LocalContext.current
    when {
        state.isLoading -> ProgressBar(title = "Getting game data...")
        state.errors  != null -> UnknownError(
            title = state.errors,
            actions = {
                TextButton(onClick = { navHostController.popBackStack() }) {
                    Text(text = "Back")
                }
            }

        ) else  -> {
            GamesLayout(state = state, events = events, navHostController = navHostController)
        }

    }

}

@Composable
fun GamesLayout(
    modifier: Modifier = Modifier,
    state: GameState,
    events: (GameEvents) -> Unit,
    navHostController: NavHostController,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        if (state.games.isEmpty()) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Games yet!")
                }
            }
        }

        items(state.games) {
            GamesCard(
               it =  it,
                onClick = {
                    navHostController.navigate(AppRouter.GamingRoute.navigate(it))
                }
            )
        }
    }
}

@Composable
fun GamesCard(
    modifier: Modifier = Modifier,
    it: Games,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            AsyncImage(
                model = it.cover,
                contentDescription = it.title,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                error = painterResource(R.drawable.game_word)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f),
                            )
                        )
                    )
            )
            Row(
                modifier = modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(it.title?:"no name", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

