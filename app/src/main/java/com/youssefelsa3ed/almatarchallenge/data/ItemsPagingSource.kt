package com.youssefelsa3ed.almatarchallenge.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youssefelsa3ed.almatarchallenge.api.Result
import com.youssefelsa3ed.almatarchallenge.api.SearchDocsModel
import com.youssefelsa3ed.almatarchallenge.api.SearchHttpProvider
import com.youssefelsa3ed.almatarchallenge.api.SearchService
import com.youssefelsa3ed.almatarchallenge.model.Doc
import java.io.IOException

class ItemsPagingSource(
    private val service: SearchService,
    private val searchKey: String,
    private val searchQuery: String,
    private val httpProvider: SearchHttpProvider
) : PagingSource<Int, Doc>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Doc> {
        val nextPageNumber = params.key ?: 1
        try {
            with(
                service.searchDocs(
                    httpProvider.provideHttpConnection(
                        SearchDocsModel(
                            searchKey,
                            searchQuery,
                            nextPageNumber
                        )
                    )
                )
            ) {
                when (this) {
                    is Result.Success -> {
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
                    is Result.Error -> {
                        return LoadResult.Error(exception)
                    }
                }
            }
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Doc>): Int? {
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