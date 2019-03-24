package com.thunderhead.searchresults.models

import org.junit.Before
import org.junit.Test

class SearchItemContainerTest {

    private var items: java.util.ArrayList<SearchItem> = ArrayList()
    private var searchItemContainer: SearchItemContainer = SearchItemContainer()

    @Before
    fun setUp() {
        items = ArrayList(1)
        items.add(SearchItem())
    }

    @Test
    fun testItems() {
        assert(searchItemContainer.items == null)
        searchItemContainer.items = items
        assert(searchItemContainer.items?.isNotEmpty() ?: false)
        val currentItems: ArrayList<SearchItem>? = searchItemContainer.items
        assert(currentItems?.equals(items) ?: false)
    }
}