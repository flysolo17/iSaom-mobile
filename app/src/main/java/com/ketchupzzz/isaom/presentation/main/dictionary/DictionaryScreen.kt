package com.ketchupzzz.isaom.presentation.main.dictionary

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.presentation.main.dictionary.words.FavoriteScreen
import com.ketchupzzz.isaom.presentation.main.dictionary.words.WordsScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    state: DictionaryState,
    events : (DictionaryEvents) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.message != null) {
            Toast.makeText(context,state.message,Toast.LENGTH_SHORT).show()
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        if (state.users == null) {
            WordsScreen(state = state, events = events)
        } else {
            val pageState = rememberPagerState(pageCount = {2})
            val scope  = rememberCoroutineScope()
            TabRow(selectedTabIndex = pageState.currentPage) {
                Tab(
                    selected = pageState.currentPage == 0,
                    text = {
                        Text(text = "Dictionary")
                    },
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(0)
                        }
                    })
                Tab(
                    selected = pageState.currentPage == 1,
                    text = {
                        Text(text = "Favorites")
                    },
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(1)
                        }
                    })
            }

            HorizontalPager(state = pageState) {page ->
                if (page == 0) {
                    WordsScreen(state = state, events = events)
                } else {
                    FavoriteScreen(
                        state = state,
                        events = events,
                    )
                }
            }
        }
    }
}

