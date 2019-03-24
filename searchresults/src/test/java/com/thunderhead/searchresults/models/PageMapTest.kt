package com.thunderhead.searchresults.models

import org.junit.Before
import org.junit.Test

class PageMapTest {

    private var images: ArrayList<CSEThumbnail> = ArrayList()
    private var pageMap: PageMap = PageMap()

    @Before
    fun setUp() {
        images = ArrayList()
        images.add(CSEThumbnail())
    }

    @Test
    fun testThumbnail() {
        assert(pageMap.images.isNullOrEmpty())
        pageMap.images = images
        assert(pageMap.images?.isNotEmpty() ?: false)
        assert((pageMap.images?.get(0) is CSEThumbnail))
    }

}