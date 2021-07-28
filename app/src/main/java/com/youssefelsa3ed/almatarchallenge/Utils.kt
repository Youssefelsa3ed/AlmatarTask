package com.youssefelsa3ed.almatarchallenge

import org.json.JSONObject

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
    ) : String {
       return if(jsonObject.isNull(key)) "" else jsonObject.getString(key)
    }
}