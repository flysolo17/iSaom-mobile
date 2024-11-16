package com.ketchupzzz.isaom.presentation.main.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.auth.User
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.presentation.routes.AppRouter


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    users: Users ? = null,
    navHostController: NavHostController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(8.dp)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Isaom", style = MaterialTheme.typography.titleLarge
                    .copy(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraLight
                    )
                )
            }
        }
        item(
            span = { GridItemSpan(2) }
        ) {
            LanguageText()
        }
        item(
            span = { GridItemSpan(2) }
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable{
                        navHostController.navigate(AppRouter.TranslatorScreen.route)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.translate),
                        contentDescription = "Image",
                        modifier = modifier.size(36.dp)
                    )
                    Text("TRANSLATOR", style = MaterialTheme.typography.titleLarge
                        .copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

        }
        item{
            FeatureCard(
                title = "Dictionary",
                icon = R.drawable.dictionary
            ) { navHostController.navigate(AppRouter.Dictionary.route) }
        }
        item {

            FeatureCard(
                title = "Sign Language",
                icon = R.drawable.la_american_sign_language_interpreting,
            ) { navHostController.navigate(AppRouter.Lessons.route)}
        }
        item {
            if (users != null) {
                val isTeacher = users.type == UserType.TEACHER
                FeatureCard(
                    title = "Classes",
                    icon = R.drawable.folder,
                    onClick = {
                        if (isTeacher) {
                            navHostController.navigate(AppRouter.TeacherDashboard.route)
                        } else {
                            navHostController.navigate(AppRouter.StudentHomeScreen.route)
                        }
                    }
                )
            }
        }
        item {
           //games here
            if (users != null) {
                FeatureCard(
                    title = "Games",
                    icon = R.drawable.game,
                ) {
                    navHostController.navigate(AppRouter.GameRoute.route)
                }
            }
        }
    }

}

@Composable
fun FeatureCard(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(2.dp, Color(0xFFFFA500)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .wrapContentSize()
            .padding(8.dp)
            .clickable { onClick() },
    ) {

        Column(
            modifier = modifier.size(129.dp).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = Color(0xFFFFA500),
                modifier = modifier.size(60.dp)
            )
            Text(title.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFFFFA500),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}
@Composable
fun LanguageText() {
    Text(
        modifier = Modifier.padding(8.dp),
        text = "• English \t • Tagalog \t • Ilocano \n Dictionary and Translator",
        style = MaterialTheme.typography.titleMedium ,
        textAlign = TextAlign.Center
    )
}
