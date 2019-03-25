package com.thunderhead.searchresults.models

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CSEThumbnailTest {

    private var url: String = "google.com.image.jpg"
    private var cseThumbnail: CSEThumbnail = CSEThumbnail()

    @Before
    fun setUp() {
        cseThumbnail = CSEThumbnail()
    }

    @Test
    fun setUrl() {
        assert(cseThumbnail.url.isNullOrEmpty())
        cseThumbnail.url = url
        assert(cseThumbnail.url?.isNotEmpty() ?: false)
        assertEquals(cseThumbnail.url, url)
    }
}