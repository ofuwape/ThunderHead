package com.thunderhead.searchresults.callbacks

import com.thunderhead.searchresults.models.SearchItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchItemDiffCallbackTest {

    private val searchItemDiffCallback: SearchItemDiffCallback = SearchItemDiffCallback()

    private var searchItemA: SearchItem = SearchItem()
    private var searchItemB: SearchItem = SearchItem()

    @Before
    fun setUp() {
        searchItemA.cacheId = "124dd8"
        searchItemA.description = "contentA"
        searchItemB.cacheId = "29d8dd8"
        searchItemB.description = "contentB"
    }

    @Test
    fun areItemsTheSame() {
        assert(searchItemDiffCallback.areItemsTheSame(searchItemA, searchItemA))
        assert(searchItemDiffCallback.areItemsTheSame(searchItemB, searchItemB))
        assertEquals(searchItemDiffCallback.areItemsTheSame(searchItemA, searchItemB), false)
    }

    @Test
    fun areContentsTheSame() {
        assert(searchItemDiffCallback.areContentsTheSame(searchItemA, searchItemA))
        assert(searchItemDiffCallback.areContentsTheSame(searchItemB, searchItemB))
        assertEquals(searchItemDiffCallback.areContentsTheSame(searchItemA, searchItemB), false)

    }
}