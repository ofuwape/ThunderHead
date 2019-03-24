package com.thunderhead.searchresults.builders

import com.thunderhead.searchresults.builders.SearchParamsBuilder.Companion.API_KEY
import com.thunderhead.searchresults.builders.SearchParamsBuilder.Companion.CX_KEY
import com.thunderhead.searchresults.builders.SearchParamsBuilder.Companion.MAX_RESULTS_KEY
import com.thunderhead.searchresults.builders.SearchParamsBuilder.Companion.NEXT_PAGE_INDEX_KEY
import com.thunderhead.searchresults.builders.SearchParamsBuilder.Companion.QUERY_KEY
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchParamsBuilderTest {

    private var query: String = "ONE Thunderhead"
    private var maxResults: Int = 5
    private var nextPageIndex: Int = 6
    private var apiKey: String = "api--key"
    private var cx: String = "c--x"

    private var searchParamsBuilder: SearchParamsBuilder = SearchParamsBuilder()

    @Before
    fun setUp() {
        searchParamsBuilder = SearchParamsBuilder()
    }

    @Test
    fun addQuery() {
        assert(searchParamsBuilder.toParams().isEmpty())
        assertEquals(searchParamsBuilder.toParams().containsValue(QUERY_KEY), false)
        searchParamsBuilder = searchParamsBuilder.addQuery(query)
        assert(searchParamsBuilder.toParams().isNotEmpty())
        assert(searchParamsBuilder.toParams().containsKey(QUERY_KEY))
        assert(searchParamsBuilder.toParams().containsValue(query))
    }

    @Test
    fun addMaxResults() {
        assert(searchParamsBuilder.toParams().isEmpty())
        assertEquals(searchParamsBuilder.toParams().containsValue(MAX_RESULTS_KEY), false)
        searchParamsBuilder = searchParamsBuilder.addMaxResults(maxResults)
        assert(searchParamsBuilder.toParams().isNotEmpty())
        assert(searchParamsBuilder.toParams().containsKey(MAX_RESULTS_KEY))
        assert(searchParamsBuilder.toParams().containsValue(maxResults.toString()))
    }

    @Test
    fun addNextPageIndex() {
        assert(searchParamsBuilder.toParams().isEmpty())
        assertEquals(searchParamsBuilder.toParams().containsValue(NEXT_PAGE_INDEX_KEY), false)
        searchParamsBuilder = searchParamsBuilder.addNextPageIndex(nextPageIndex)
        assert(searchParamsBuilder.toParams().isNotEmpty())
        assert(searchParamsBuilder.toParams().containsKey(NEXT_PAGE_INDEX_KEY))
        assert(searchParamsBuilder.toParams().containsValue(nextPageIndex.toString()))
    }

    @Test
    fun addAPIKey() {
        assert(searchParamsBuilder.toParams().isEmpty())
        assertEquals(searchParamsBuilder.toParams().containsValue(API_KEY), false)
        searchParamsBuilder = searchParamsBuilder.addAPIKey(apiKey)
        assert(searchParamsBuilder.toParams().isNotEmpty())
        assert(searchParamsBuilder.toParams().containsKey(API_KEY))
        assert(searchParamsBuilder.toParams().containsValue(apiKey))
    }

    @Test
    fun addCX() {
        assert(searchParamsBuilder.toParams().isEmpty())
        assertEquals(searchParamsBuilder.toParams().containsValue(CX_KEY), false)
        searchParamsBuilder = searchParamsBuilder.addCX(cx)
        assert(searchParamsBuilder.toParams().isNotEmpty())
        assert(searchParamsBuilder.toParams().containsKey(CX_KEY))
        assert(searchParamsBuilder.toParams().containsValue(cx))
    }

}