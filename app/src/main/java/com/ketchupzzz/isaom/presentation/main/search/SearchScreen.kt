package com.ketchupzzz.isaom.presentation.main.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.trace
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.presentation.main.dictionary.words.WordCard

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state : SearchScreenState,
    events : (SearchScreenEvents) -> Unit,
    navHostController: NavHostController
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
                TextField(
                    modifier = modifier.weight(1f),
                    shape = RoundedCornerShape(32.dp),
                    textStyle = TextStyle(
                        fontSize = 12.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    placeholder = { Text("Search word here...") },
                    value = state.filter,
                    onValueChange = { events(SearchScreenEvents.OnFilterChanged(it))},
                    trailingIcon = {
                        IconButton(onClick = {
                            events(SearchScreenEvents.OnSearch(state.filter))
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                )
            }
        }
        item {
            HorizontalDivider(modifier.fillMaxWidth())
        }
        when {
            state.isLoading -> {
                item {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            state.error  != null -> {
              item {
                  Box(
                      modifier = modifier.fillMaxSize(),
                      contentAlignment = Alignment.Center
                  ) {
                      Text(state.error)
                  }
              }
            } else -> {
                items(state.words) {
                    WordCard(dictionary = it, onAddToFavorites = {})
                }
            }
        }
    }
}
