package com.youssefelsa3ed.almatarchallenge.api

import com.youssefelsa3ed.almatarchallenge.model.Doc
import com.youssefelsa3ed.almatarchallenge.model.SearchResponse
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchResponseParserTest {

    @Test
    fun `parse - empty json object - expect empty list throw exception`() {
        val emptyJsonObject = JSONObject()
        val actualResult = SearchResponseParser.parse(emptyJsonObject)

        assertEquals(actualResult.size, 0)
    }

    @Test
    fun `parse - json object with docs - search response object empty docs`() {
        val jsonObject = JSONObject()
        jsonObject.put("", "")
        jsonObject.put("", "")

        val jsonArray = JSONArray()
        jsonArray.put(jsonObject)

        val mainObj = JSONObject()
        mainObj.put("docs", jsonArray)

        val actualResult = SearchResponseParser.parse(mainObj)
        val doc = Doc(
            emptyList(), emptyList(), emptyList(),
            "", ""
        )
        val result = SearchResponse(
            docs = List(1) {
                doc
            }
        ).docs
        assertEquals(actualResult, result)
    }

    @Test
    fun `parse - json object with docs - search response object author_name`() {
        val str = "{\"docs\":[{\"author_name\":[\"some-name\"]}]}"
        val jsonObject = JSONObject(str)
        val actualResult = SearchResponseParser.parse(jsonObject)
        val doc = Doc(
            listOf("some-name"), emptyList(), emptyList(),
            "", ""
        )
        val result = SearchResponse(
            docs = List(1) {
                doc
            }
        ).docs
        assertEquals(actualResult, result)
    }

    @Test
    fun `parse - json object with docs - search response object author_key`() {
        val str = "{\"docs\":[{\"author_key\":[\"some-key\"]}]}"
        val jsonObject = JSONObject(str)
        val actualResult = SearchResponseParser.parse(jsonObject)
        val doc = Doc(
            emptyList(), listOf("some-key"), emptyList(),
            "", ""
        )
        val result = SearchResponse(
            docs = List(1) {
                doc
            }
        ).docs
        assertEquals(actualResult, result)
    }

    @Test
    fun `parse - json object with docs - search response object isbn`() {
        val str = "{\"docs\":[{\"isbn\":[\"some-isbn\"]}]}"
        val jsonObject = JSONObject(str)
        val actualResult = SearchResponseParser.parse(jsonObject)
        val doc = Doc(
            emptyList(), emptyList(), listOf("some-isbn"),
            "", ""
        )
        val result = SearchResponse(
            docs = List(1) {
                doc
            }
        ).docs
        assertEquals(actualResult, result)
    }

    @Test
    fun `parse - json object with docs - search response object 3 values`() {
        val str =
            "{\"docs\":[{\"author_name\":[\"some-name\"],\"isbn\":[\"some-isbn\"],\"author_key\":[\"some-key\"]}]}"
        val jsonObject = JSONObject(str)
        val actualResult = SearchResponseParser.parse(jsonObject)
        val doc = Doc(
            listOf("some-name"), listOf("some-key"), listOf("some-isbn"),
            "", ""
        )
        val result = SearchResponse(
            docs = List(1) {
                doc
            }
        ).docs
        assertEquals(actualResult, result)
    }
}