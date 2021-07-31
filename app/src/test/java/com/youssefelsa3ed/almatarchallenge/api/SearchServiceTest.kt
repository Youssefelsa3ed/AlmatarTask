package com.youssefelsa3ed.almatarchallenge.api

import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import java.net.HttpURLConnection

class SearchServiceTest {

    @Test
    fun `searchDocs - response code is HTTP_OK`() = runBlocking {
        val searchHttpProvider = SearchHttpProvider()
        val searchService = SearchService()
        val actualValue = searchService.searchDocs(
            searchHttpProvider.provideHttpConnection(
                SearchDocsModel(queryVal = "")
            )
        )
        val result = Result.Success(SearchResponseParser.parse(JSONObject("{}")))

        assertEquals(actualValue, eq(result))
    }

    @Test
    fun `searchDocs - response code is HTTP_FAILED`() = runBlocking {
        val failedConnection: HttpURLConnection = mock()
        val searchService = SearchService()
        val actualValue = searchService.searchDocs(failedConnection)
        val result = Result.Error(Exception(failedConnection.responseMessage))
        assertEquals(actualValue.toString(), eq(result.toString()))
    }
}