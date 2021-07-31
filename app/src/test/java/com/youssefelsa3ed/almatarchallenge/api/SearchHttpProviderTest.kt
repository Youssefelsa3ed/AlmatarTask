package com.youssefelsa3ed.almatarchallenge.api

import com.youssefelsa3ed.almatarchallenge.utils.URLS
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SearchHttpProviderTest {
    private val model = SearchDocsModel(queryVal = "some-value")

    @Test
    fun `provideHttpConnection - url`() {

        val searchProvider = SearchHttpProvider()
        val httpConnection = searchProvider.provideHttpConnection(model = model)
        val expectedValue = URL(
            URLS.SearchURL.value +
                    "?${model.queryKey}=${URLEncoder.encode(model.queryVal, "UTF-8")}" +
                    "&page=${model.page}"
        )
        assertEquals(httpConnection.url, expectedValue)
    }

    @Test
    fun `provideHttpConnection - responseCode`() {
        val searchProvider = SearchHttpProvider()
        val httpConnection = searchProvider.provideHttpConnection(model = model)

        assertEquals(httpConnection.responseCode, HttpURLConnection.HTTP_OK)
    }

    @Test
    fun `provideHttpConnection - requestMethod`() {
        val searchProvider = SearchHttpProvider()
        val httpConnection = searchProvider.provideHttpConnection(model = model)

        assertEquals(httpConnection.requestMethod, "GET")
    }
}