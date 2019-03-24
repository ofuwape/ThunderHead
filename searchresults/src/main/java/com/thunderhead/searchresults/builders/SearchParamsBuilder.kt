package com.thunderhead.searchresults.builders

import java.util.*

/**
 * A convenience builder class for creating a params map for searching the Curiosity API.
 */
class SearchParamsBuilder {

    private val mParams = HashMap<String, String>()


    fun addTerm(term: String?): SearchParamsBuilder {
        if (term != null) {
            mParams["term"] = term
        }
        return this
    }

    fun addLimit(limit: Int): SearchParamsBuilder {
        mParams["limit"] = Integer.toString(limit)
        return this
    }

    fun toParams(): Map<String, String> {
        return mParams
    }

}
