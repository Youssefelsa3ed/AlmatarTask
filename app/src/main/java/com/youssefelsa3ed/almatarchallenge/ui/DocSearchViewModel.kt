package com.youssefelsa3ed.almatarchallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.youssefelsa3ed.almatarchallenge.data.SearchRepository
import com.youssefelsa3ed.almatarchallenge.model.Doc
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel for the [DocListFragment] screen.
 * The ViewModel works with the [SearchRepository] to get the data.
 */
class DocSearchViewModel(
    private val repository: SearchRepository,
) : ViewModel() {
    var currentQueryKey: String? = null
    var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Doc>>? = null

    fun searchRepo(queryKey: String, queryVal: String): Flow<PagingData<Doc>> {
        val lastResult = currentSearchResult
        if (queryKey == currentQueryKey && queryVal == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryKey = queryKey
        currentQueryValue = queryVal
        val newResult: Flow<PagingData<Doc>> = repository.getSearchResultStream(queryKey, queryVal)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}