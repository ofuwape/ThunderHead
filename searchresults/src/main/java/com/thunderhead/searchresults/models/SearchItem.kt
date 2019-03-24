package com.thunderhead.searchresults.models

import com.google.gson.annotations.SerializedName

class SearchItem {

    @SerializedName("title")
    var title: String? = null

    @SerializedName("snippet")
    var description: String? = null

    @SerializedName("formattedUrl")
    var url: String? = null

    @SerializedName("pagemap")
    var pageMap: PageMap? = null

    @SerializedName("cacheId")
    var cacheId: String? = null


}
