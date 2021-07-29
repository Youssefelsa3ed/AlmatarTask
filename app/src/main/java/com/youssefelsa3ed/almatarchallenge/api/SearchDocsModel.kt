package com.youssefelsa3ed.almatarchallenge.api

data class SearchDocsModel(
    val queryKey: String = "q",
    val queryVal: String,
    val page: Int = 1
)
