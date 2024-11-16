package com.ketchupzzz.isaom.models.dictionary.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


class DictionaryPagingSource(
    private val query: Query
) : PagingSource<DocumentSnapshot, Dictionary>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Dictionary> {
        return try {
            val currentPage = params.key
            val snapshot = if (currentPage == null) {
                query.limit(params.loadSize.toLong()).get().await()
            } else {
                query.startAfter(currentPage).limit(params.loadSize.toLong()).get().await()
            }

            val entries = snapshot.toObjects(Dictionary::class.java)
            Log.d("dictionary", "loaded ${snapshot.documents.lastOrNull()?.id}")

            LoadResult.Page(
                data = entries,
                prevKey = null,
                nextKey = snapshot.documents.lastOrNull()
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Dictionary>): DocumentSnapshot? {
        val anchorPosition = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchorPosition) ?: return null

        return closestPage.prevKey ?: closestPage.nextKey
    }



}
