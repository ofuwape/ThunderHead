package com.thunderhead.searchresults.models

import org.junit.Before
import org.junit.Test

class SearchItemTest {

    private var title: String? = null
    private var description: String? = null
    private var url: String? = null
    private var pageMap: PageMap? = null
    private var cacheId: String? = null

    private var searchItem: SearchItem = SearchItem()

    @Before
    fun setUp() {
        title = "title"
        description = "description"
        url = "google.com"
        pageMap = PageMap()
        cacheId = "12ed92"
        searchItem = SearchItem()
    }

    @Test
    fun testTitle() {
        assert(searchItem.title.isNullOrBlank())
        searchItem.title = title
        assert(searchItem.title?.isNotEmpty() ?: false)
        assert(searchItem.title.equals(title))
    }

    @Test
    fun testDescription() {
        assert(searchItem.description.isNullOrBlank())
        searchItem.description = description
        assert(searchItem.description?.isNotEmpty() ?: false)
        assert(searchItem.description.equals(description))
    }

    @Test
    fun testUrl() {
        assert(searchItem.url.isNullOrBlank())
        searchItem.url = url
        assert(searchItem.url?.isNotEmpty() ?: false)
        assert(searchItem.url.equals(url))
    }

    @Test
    fun testPageMap() {
        assert(searchItem.pageMap == null)
        searchItem.pageMap = pageMap
        assert(searchItem.pageMap != null)
        assert(searchItem.pageMap?.equals(pageMap) ?: false)
    }

    @Test
    fun testCacheId() {
        assert(searchItem.cacheId.isNullOrBlank())
        searchItem.cacheId = cacheId
        assert(searchItem.cacheId?.isNotEmpty() ?: false)
        assert(searchItem.cacheId.equals(cacheId))
    }

}