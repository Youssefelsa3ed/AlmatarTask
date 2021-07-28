package com.youssefelsa3ed.almatarchallenge.api

import com.youssefelsa3ed.almatarchallenge.model.Doc

/**
 * Result from a search, which contains List<[Doc]> holding query data,
 * and a String of network error state.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}