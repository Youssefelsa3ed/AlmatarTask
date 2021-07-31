package com.youssefelsa3ed.almatarchallenge.api

import com.youssefelsa3ed.almatarchallenge.model.Doc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection

class SearchService {
    /***
     *
     * Search the OpenLibrary API by documents titles and authors.
     *
     * @param connection a [HttpURLConnection] The Connection Request Object.
     *
     * @return The response of the search query as a list of [Doc], and an [Exception] if anything went wrong.
     */
    suspend fun searchDocs(
        connection: HttpURLConnection
    ): Result<List<Doc>> {
        return withContext(Dispatchers.IO) {
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream
                    .bufferedReader()
                    .use { it.readText() }
                Result.Success(SearchResponseParser.parse(JSONObject(response)))
            } else
                Result.Error(Exception(connection.responseMessage))
        }
    }
}