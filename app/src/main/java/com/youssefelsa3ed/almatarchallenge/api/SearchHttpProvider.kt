package com.youssefelsa3ed.almatarchallenge.api

import com.youssefelsa3ed.almatarchallenge.utils.URLS
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SearchHttpProvider {

    fun provideHttpConnection(
        model: SearchDocsModel
    ): HttpURLConnection {
        val httpURLConnection = getURL(model).openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = false
        return httpURLConnection
    }

    private fun getURL(model: SearchDocsModel) = URL(
        URLS.SearchURL.value +
                "?${model.queryKey}=${URLEncoder.encode(model.queryVal, "UTF-8")}" +
                "&page=${model.page}"
    )
}