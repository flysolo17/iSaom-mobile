package com.ketchupzzz.isaom.presentation.auth.register.gender

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.presentation.auth.register.RegisterEvents
import com.ketchupzzz.isaom.presentation.auth.register.RegisterState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderScreen(
    modifier: Modifier = Modifier,
    state: RegisterState,
    onSelected : (selectedGender : Gender,url : String) -> Unit

) {


    val genderSelection = state.genderSelection
    val combinedList = mutableListOf<Pair<String, Gender>>()
    val maxSize = maxOf(genderSelection.females.url.size, genderSelection.males.url.size)

    for (i in 0 until maxSize) {
        if (i < genderSelection.females.url.size) {
            combinedList.add(Pair(genderSelection.females.url[i], genderSelection.females.gender))
        }
        if (i < genderSelection.males.url.size) {
            combinedList.add(Pair(genderSelection.males.url[i], genderSelection.males.gender))
        }
    }


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            userScrollEnabled = true // Enable scrolling if needed
        ) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        modifier = Modifier
                            .width(150.dp)
                            .height(150.dp),
                        contentDescription = stringResource(R.string.logo)
                    )
                    Text(
                        text = "Please choose your gender",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            items(combinedList.size) { index ->
                val (url, gender) = combinedList[index]
                GenderItem(
                    url = url,
                    gender = gender,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    onClick = {
                        onSelected.invoke(gender,url)
                    }
                )
            }
        }
}

@Composable
fun GenderItem(
    url: String,
    gender: Gender,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = url,
            contentDescription = "$gender image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}

