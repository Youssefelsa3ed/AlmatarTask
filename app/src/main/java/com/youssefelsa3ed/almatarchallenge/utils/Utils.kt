package com.youssefelsa3ed.almatarchallenge.utils

import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.HttpRetryException
import java.net.URL

object Utils {
    /***
     *
     * Fills a list of [String] with items extracted from a [JSONObject].
     *
     * @param list the list to be filled with the items.
     * @param jsonObject the [JSONObject] to extract data from.
     * @param key The key of the list in the Json Object.
     *
     * @return list of Strings
     */
    fun setStringItemsToList(
        list: MutableList<String>,
        jsonObject: JSONObject,
        key: String
    ) {
        jsonObject.optJSONArray(key)?.let {
            for (i in 0 until it.length())
                list.add(it.getString(i))
        }
    }

    /***
     *
     * Asynchronously load an image using while also returning the corresponding message to the loading request.
     *
     * @param url The url of the image.
     *
     * @return a Pair of response message as a [String] and the [InputStream] of the image if loaded successfully, null otherwise.
     */
    fun getImageStreamFromUrl(
        url: String
    ): Pair<String?, InputStream?> {
        if (url.isEmpty()) return "Url shouldn't be empty!" to null
        val errorMsg: String?
        try {
            val inputStream = URL(url).openStream()
            return "Image loaded successfully" to inputStream
        } catch (e: Exception) {
            errorMsg = when (e) {
                is HttpRetryException -> {
                    when {
                        e.responseCode() == 403 -> "You exceeded api call limit"
                        e.responseCode() == 404 -> "Invalid URL!"
                        else -> e.message
                    }
                }
                is FileNotFoundException -> "Invalid URL!"
                else -> e.message
            }
        }
        return errorMsg to null
    }

    /***
     *
     * Check if an  is null or not.
     *
     * @param obj The object to check.
     *
     * @return true if the object is null, false otherwise.
     */
    fun isObjectNull(
        obj: Any?
    ): Boolean {
        return obj == null
    }
}