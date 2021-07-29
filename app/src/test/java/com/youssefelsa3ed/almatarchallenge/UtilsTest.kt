package com.youssefelsa3ed.almatarchallenge

import junit.framework.TestCase
import org.json.JSONObject
import org.junit.Test
import org.mockito.Mockito

class UtilsTest : TestCase() {

    fun testSetStringItemsToList_Empty() {
        val key = "array"
        val jsonObject = Mockito.mock(JSONObject::class.java)
        val list = mutableListOf<String>()

        Utils.setStringItemsToList(list, jsonObject, key)

        assert(list.isEmpty())
    }

    @Test
    fun testSetBitmapFromEmptyUrl() {
        val url = ""

        val response = Utils.getImageStreamFromUrl(url)

        assertEquals(response.first, "Url shouldn't be empty!")
        assertEquals(response.second, null)
    }

    @Test
    fun testSetBitmapFromActualUrl_Valid() {
        val url = Utils.getIsbnImageUrl("9780618346240")

        val response = Utils.getImageStreamFromUrl(url)

        assertEquals(response.first, "Image loaded successfully")
        assert(response.second != null)
    }

    @Test
    fun testSetBitmapFromActualUrl_Invalid() {
        val url = Utils.getIsbnImageUrl("")

        val response = Utils.getImageStreamFromUrl(url)

        assertEquals(response.first, "Invalid URL!")
        assertEquals(response.second, null)
    }

    @Test
    fun testIsObjectNotNull() {
        val text: Any = ""

        assertEquals(Utils.isObjectNull(text), false)
    }

    @Test
    fun testIsObjectNull() {
        val any: Any? = null

        assertEquals(Utils.isObjectNull(any), true)
    }
}