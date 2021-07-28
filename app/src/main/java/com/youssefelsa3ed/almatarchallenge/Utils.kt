package com.youssefelsa3ed.almatarchallenge

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONObject
import java.io.FileNotFoundException
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
        for (i in 0 until jsonObject.getJSONArray(key).length())
            list.add(jsonObject.getJSONArray(key).getString(i))
    }

    /***
     *
     * Check if a [JSONObject] contains a specific key or not.
     *
     * @param jsonObject the [JSONObject] to extract data from.
     * @param key The key of the [String] item in the Json Object.
     *
     * @return a [String] if the json object has this key, empty [String] otherwise
     */
    fun getStringFromJsonObject(
        jsonObject: JSONObject,
        key: String
    ): String {
        return if (jsonObject.isNull(key)) "" else jsonObject.getString(key)
    }

    /***
     *
     * Asynchronously load an image using while also returning the corresponding message to the loading request.
     *
     * @param url The url of the image.
     *
     * @return a Pair of response message as a [String] and the [Bitmap] of the image if loaded successfully, null otherwise.
     */
    fun setBitmapFromUrl(
        url: String
    ): Pair<String?, Bitmap?> {
        val errorMsg: String?
        try {
            val inputStream = URL("${url}?default=false").openStream()
            return "Image loaded successfully" to BitmapFactory.decodeStream(inputStream)
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