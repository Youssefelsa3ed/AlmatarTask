package com.youssefelsa3ed.almatarchallenge.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youssefelsa3ed.almatarchallenge.api.RepoSearchResult
import com.youssefelsa3ed.almatarchallenge.api.SearchBooksModel
import com.youssefelsa3ed.almatarchallenge.api.SearchService
import com.youssefelsa3ed.almatarchallenge.model.Docs

class ItemsPagingSource(
    private val backend: SearchService,
    private val queryKey: String,
    private val queryVal: String,
) : PagingSource<Int, Docs>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Docs> {
        val nextPageNumber = params.key ?: 1
        with(backend.searchBooks(
            SearchBooksModel(
                queryKey,
                queryVal,
                nextPageNumber
            ))
        ) {
            when (this) {
                is RepoSearchResult.Success -> {
                    val nextKey = if (data.isNullOrEmpty())
                        null
                    else
                        nextPageNumber + 1
                    return LoadResult.Page(
                        data = data,
                        prevKey = null, // Only paging forward.
                        nextKey = nextKey
                    )
                }
                is RepoSearchResult.Error -> {
                    return LoadResult.Error(exception)
                }
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Docs>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}