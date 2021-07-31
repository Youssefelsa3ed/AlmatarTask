package com.youssefelsa3ed.almatarchallenge.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import com.youssefelsa3ed.almatarchallenge.api.SearchHttpProvider
import com.youssefelsa3ed.almatarchallenge.api.SearchService
import com.youssefelsa3ed.almatarchallenge.model.Doc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val NETWORK_PAGE_SIZE = 100

/**
 * Repository class that works with the remote data sources.
 */
class SearchRepository(
    private val searchService: SearchService,
    private val searchHttpProvider: SearchHttpProvider
) {
    /**
     * Search for documents/authors whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getSearchResultStream(queryKey: String, queryVal: String): Flow<PagingData<Doc>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ItemsPagingSource(
                    service = searchService,
                    searchKey = queryKey,
                    searchQuery = queryVal,
                    httpProvider = searchHttpProvider
                )
            }
        ).flow.map {
            it.filter { doc: Doc ->
                !doc.authorName.isNullOrEmpty()
            }
        }
    }
}
