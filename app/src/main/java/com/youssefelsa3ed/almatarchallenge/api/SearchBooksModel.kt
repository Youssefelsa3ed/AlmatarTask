package com.youssefelsa3ed.almatarchallenge.api

data class SearchBooksModel(
    val queryKey: String = "q",
    val queryVal: String,
    val page: Int = 1
)
