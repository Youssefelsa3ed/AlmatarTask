package com.youssefelsa3ed.almatarchallenge.api

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchDocsModelTest {

    @Test
    fun `query key - expected q`() {
        val searchModel = SearchDocsModel(queryVal = "some-value")
        assertEquals(searchModel.queryKey, "q")
    }

    @Test
    fun `page - expected 1`() {
        val searchModel = SearchDocsModel(queryVal = "some-value")
        assertEquals(searchModel.page, 1)
    }
}