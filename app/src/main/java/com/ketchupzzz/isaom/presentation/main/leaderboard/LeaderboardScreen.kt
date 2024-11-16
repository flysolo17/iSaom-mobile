package com.ketchupzzz.isaom.presentation.main.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.games.GameSubmission
import com.ketchupzzz.isaom.utils.ProfileImage
import com.ketchupzzz.isaom.utils.toast


@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    state: LeaderboardState,
    events: (LeaderboardEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
    }
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
    ) {

        itemsIndexed(state.submissions) { index ,data ->
            UserScoreCard(
                user = data.user,
                submissions = data.submission,
                totalScore = data.totalScore,
                index = index + 1
            )
        }
    }
}

@Composable
fun UserScoreCard(
    modifier: Modifier = Modifier,
    index : Int,
    user: Users,
    submissions: List<GameSubmission>,
    totalScore: Int
) {
   Card(
       modifier = modifier.fillMaxWidth().padding(8.dp),
       elevation = CardDefaults.cardElevation(
           4.dp
       ),
       shape = MaterialTheme.shapes.small
   ) {
       Row(
           modifier = modifier.fillMaxWidth().padding(16.dp),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.spacedBy(8.dp)
       ) {
           Text(
               "$index",
               style = MaterialTheme.typography.titleMedium
           )
           ProfileImage(
               imageURL = user.avatar ?: "",
           ) { }
           Column(
               modifier = modifier.fillMaxWidth().weight(1f)
           ) {
               Text(
                   user.name ?: "no name",
                   style = MaterialTheme.typography.titleMedium
               )
               Text(
                   user.type?.name ?: "no type",
                   style = MaterialTheme.typography.titleSmall.copy(
                       color = Color.Gray
                   )
               )
           }
           Column(
               horizontalAlignment = Alignment.End
           ) {
               Text(
                   "$totalScore",
                   style = MaterialTheme.typography.titleLarge.copy(
                       color = MaterialTheme.colorScheme.primary
                   )
               )
               Text(
                   "${submissions.size} matches",
                   style = MaterialTheme.typography.labelSmall.copy(
                       color = Color.Gray
                   )
               )
           }
       }
   }
}
