package com.thunderhead.searchresults.models

import com.google.gson.annotations.SerializedName

class PageMap {

    @SerializedName("cse_thumbnail")
    var images: ArrayList<CSEThumbnail>? = null
}