package com.youssefelsa3ed.almatarchallenge.api

import com.youssefelsa3ed.almatarchallenge.Utils
import com.youssefelsa3ed.almatarchallenge.model.Doc
import com.youssefelsa3ed.almatarchallenge.model.SearchResponse
import org.json.JSONObject

object SearchResponseParser {
    /***
     *
     * Map a jsonObject to it's equivalent [SearchResponse] object and extract the list of [Doc] from it.
     *
     * @param jsonObject the Json Object to extract the list from.
     *
     * @return List<[Doc]> extracted from the Json object.
     */
    fun parse(jsonObject: JSONObject): List<Doc> {
        return try {
            SearchResponse(
                docs = List(jsonObject.getJSONArray("docs").length()) {
                    val item = jsonObject.getJSONArray("docs").get(it) as JSONObject

                    val authors = mutableListOf<String>()
                    item.optJSONArray("author_name")?.let {
                        Utils.setStringItemsToList(authors, item, "author_name")
                    }

                    val keys = mutableListOf<String>()
                    item.optJSONArray("author_key")?.let {
                        Utils.setStringItemsToList(keys, item, "author_key")
                    }

                    val isbns = mutableListOf<String>()
                    item.optJSONArray("isbn")?.let {
                        Utils.setStringItemsToList(isbns, item, "isbn")
                    }

                    Doc(
                        authors, keys, isbns,
                        item.optString("title"),
                        item.optString("key")
                    )
                }
            ).docs
        } catch (e: Exception) {
            listOf()
        }
    }
}