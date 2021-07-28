package com.youssefelsa3ed.almatarchallenge.api

import com.youssefelsa3ed.almatarchallenge.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private const val searchURL = "http://openlibrary.org/search.json"

class SearchService {
    /***
     *
     * Search the OpenLibrary API by documents titles and authors.
     *
     * @param model a [SearchBooksModel] object contains the query parameters.
     *
     * @return The response of the search query as an [SearchResponse], and an [Exception] if anything went wrong.
     */
    suspend fun searchBooks(
        model: SearchBooksModel,
    ): RepoSearchResult {
        val url = URL(searchURL +
                "?${model.queryKey}=${URLEncoder.encode(model.queryVal, "UTF-8")}" +
                "&page=${model.page}")
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = false
        // Check if the connection is successful
        return withContext(Dispatchers.IO) {
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                RepoSearchResult.Success(SearchResponseParser.parse(JSONObject(response)))
            } else
                RepoSearchResult.Error(Exception(httpURLConnection.responseMessage))
        }
    }
}