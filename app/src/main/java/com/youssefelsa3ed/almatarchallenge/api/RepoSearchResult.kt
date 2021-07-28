package com.youssefelsa3ed.almatarchallenge.api

import com.youssefelsa3ed.almatarchallenge.model.Docs

/**
 * RepoSearchResult from a search, which contains List<[Docs]> holding query data,
 * and a String of network error state.
 */
sealed class RepoSearchResult {
    data class Success(val data: List<Docs>) : RepoSearchResult()
    data class Error(val exception: Exception) : RepoSearchResult()
}