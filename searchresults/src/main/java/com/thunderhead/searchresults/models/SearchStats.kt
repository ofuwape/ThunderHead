package com.thunderhead.searchresults.models

import com.google.gson.annotations.SerializedName

class SearchStats {

    @SerializedName("searchTime")
    val searchTime: Float = 0f

    @SerializedName("totalResults")
    val totalResults: Int = 0
}