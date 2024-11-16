package com.ketchupzzz.isaom.presentation.main.teacher.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.sections.Sections
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.toast


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,

    state: DashboardState,
    events: (DashboardEvents) -> Unit,
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        if (state.users != null) {
            events(DashboardEvents.OnGetAllSectionWithSubjects(sections = state.users.sections))
        }
    }
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
    }
    when {
        state.isLoading -> ProgressBar(
            title = "Getting Teachers Data..."
        )
        !state.isLoading && state.sections.isEmpty() -> {
            NoSectionYet()
        } else -> {
            LazyColumn(
                modifier = modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                item {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "My Classes (${state.sections.size})",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                    }

                }
                items(state.sections) {
                    SectionWithStudentsCard(
                        sections = it,
                        onClick = {
                            navHostController.navigate(AppRouter.ViewSection.createRoute(it.id ?:""))
                        }
                    )
                }
                
            }
        }
    }
}

@Composable
fun SectionWithStudentsCard(
    modifier: Modifier = Modifier,
    sections: Sections,
    onClick : () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "${sections?.name}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )
            Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos, contentDescription = "null")
        }
    }
}

@Composable
fun NoSectionYet(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.resource_class),
            contentDescription = "Class")
        Text(
            modifier =  modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            text = "No class yet please coordinate to admin to set your class!",
            fontWeight = FontWeight.Bold
        )
    }
}

