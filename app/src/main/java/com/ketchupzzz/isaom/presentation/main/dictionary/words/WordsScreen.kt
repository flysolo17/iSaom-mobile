package com.ketchupzzz.isaom.presentation.main.dictionary.words

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.dictionary.remote.Dictionary
import com.ketchupzzz.isaom.presentation.main.dictionary.DictionaryEvents
import com.ketchupzzz.isaom.presentation.main.dictionary.DictionaryState
import com.ketchupzzz.isaom.ui.custom.WebViewDialog
import com.ketchupzzz.isaom.utils.generateRandomString
import com.ketchupzzz.isaom.utils.toast

@Composable
fun WordsScreen(
    modifier: Modifier = Modifier,
    state: DictionaryState,
    events: (DictionaryEvents) -> Unit
) {
    val context = LocalContext.current
    val lazyPagingItems = state.dictionaryList.collectAsLazyPagingItems()

    LaunchedEffect(state) {
        if (state.message != null) {
            context.toast(state.message)

        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            Box(
                modifier = modifier.fillMaxWidth().padding(12.dp),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }

        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                items(lazyPagingItems) { dictionaryEntry ->
                    val isEnabled = state.users != null
                    dictionaryEntry?.let {
                        WordCard(dictionary = it, onAddToFavorites = {
                            events.invoke(DictionaryEvents.OnAddToFavorites(it))
                        }, isButtonEnable = isEnabled)
                    }
                }
                lazyPagingItems.apply {
                    when (loadState.append) {
                        LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = modifier.fillMaxWidth().padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    CircularProgressIndicator()
                                }

                            }
                        }
                        is LoadState.Error -> {
                            item {
                                val e = lazyPagingItems.loadState.append as LoadState.Error
                                Text(
                                    text = "Error: ${e.error.localizedMessage}",
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        is LoadState.NotLoading -> {}
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewWord(
    modifier: Modifier = Modifier,
    dictionary: Dictionary,
    onDismiss : () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "English" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
            Text(text = dictionary.word ?: "No Word" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
            Text(text = "Ilocano" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
            Text(text = dictionary.definition ?: "No Word" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
            Text(text = "Tagalog" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
            Text(text = dictionary.tagalog ?: "No Word" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
            Text(text = "Source" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)

        }
    }
}
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text : String ,
    onSearching : (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = text,
        onValueChange = {onSearching(it)},
        shape = RoundedCornerShape(16.dp),
        label = { Text("Search word here...") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "search") }
    )
}

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    dictionary: Dictionary,
    onAddToFavorites : () -> Unit,
    isButtonEnable : Boolean = false,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = modifier
                    .fillMaxWidth()
                    .weight(1f), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "English" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
                    Text(text = dictionary.word ?: "No Word" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
                }

                if (isButtonEnable) {
                    IconButton(onClick = { onAddToFavorites() }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_star_outline_24), contentDescription = "Not Liked")
                    }
                }
            }

            Spacer(modifier = modifier.height(2.dp))
            HorizontalDivider()
            Spacer(modifier = modifier.height(2.dp))
            Text(text = "Ilocano" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
            Text(text = dictionary.definition ?: "No Definition" , color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
        }
    }
}

fun openLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(intent)
}