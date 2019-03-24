package com.thunderhead.searchresults.builders

import java.util.*

/**
 * A convenience builder class for creating a params map for searching the Custom Google Search API.
 */
class SearchParamsBuilder {

    private val mParams = HashMap<String, String>()

    fun addQuery(term: String?): SearchParamsBuilder {
        if (term != null) {
            mParams[QUERY_KEY] = term
        }
        return this
    }

    fun addMaxResults(limit: Int): SearchParamsBuilder {
        mParams[MAX_RESULTS_KEY] = Integer.toString(limit)
        return this
    }

    fun addNextPageIndex(nextPageIndex: Int): SearchParamsBuilder {
        mParams[NEXT_PAGE_INDEX_KEY] = Integer.toString(nextPageIndex)
        return this
    }

    fun addAPIKey(key: String?): SearchParamsBuilder {
        if (key != null) {
            mParams[API_KEY] = key
        }
        return this
    }

    fun addCX(cx: String?): SearchParamsBuilder {
        if (cx != null) {
            mParams[CX_KEY] = cx
        }
        return this
    }

    fun toParams(): Map<String, String> {
        return mParams
    }

    companion object {
        const val QUERY_KEY: String = "q"
        const val MAX_RESULTS_KEY: String = "num"
        const val NEXT_PAGE_INDEX_KEY: String = "start"
        const val API_KEY: String = "key"
        const val CX_KEY: String = "cx"
    }

}
