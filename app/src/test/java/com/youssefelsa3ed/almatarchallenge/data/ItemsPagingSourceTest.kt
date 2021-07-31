package com.youssefelsa3ed.almatarchallenge.data

import androidx.paging.PagingSource
import com.youssefelsa3ed.almatarchallenge.api.SearchDocsModel
import com.youssefelsa3ed.almatarchallenge.api.SearchHttpProvider
import com.youssefelsa3ed.almatarchallenge.api.SearchService
import com.youssefelsa3ed.almatarchallenge.model.Doc
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemsPagingSourceTest {
    private val searchProvider = SearchHttpProvider()
    private val searchService = SearchService()

    @Test
    fun `load paging data - title successful load`() = runBlocking {
        val model = SearchDocsModel(queryVal = "Angles And Demons")
        val pagingSource =
            ItemsPagingSource(searchService, model.queryKey, model.queryVal, searchProvider)
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    Doc(
                        authorName = listOf("Rene Chandelle"),
                        authorKey = listOf("OL2829632A"),
                        isbn = listOf("9780785821854", "0785821856"),
                        title = "Beyond Angles And Demons",
                        key = "/works/OL8476737W"
                    )
                ),
                prevKey = null,
                nextKey = 2,
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ),
        )
    }

    @Test
    fun `load paging data - author successful load`() = runBlocking {
        val model = SearchDocsModel(queryKey = "author", queryVal = "Fairy birds")
        val pagingSource =
            ItemsPagingSource(searchService, model.queryKey, model.queryVal, searchProvider)
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    Doc(
                        authorName = listOf("Fairy birds"),
                        authorKey = listOf("OL6063640A"),
                        isbn = emptyList(),
                        title = "Fairy birds from Fancy islet; or, The children in the forest, by the author of 'The gipsies'",
                        key = "/works/OL13160075W"
                    )
                ),
                prevKey = null,
                nextKey = 2,
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ),
        )
    }

    @Test
    fun `load paging data - empty search`() = runBlocking {
        val model = SearchDocsModel(queryVal = "")
        val pagingSource =
            ItemsPagingSource(searchService, model.queryKey, model.queryVal, searchProvider)
        assertEquals(
            PagingSource.LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null,
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 0,
                    placeholdersEnabled = false
                )
            ),
        )
    }
}