package com.thunderhead.searchresults.models

import com.google.gson.annotations.SerializedName

class SearchItemContainer {

    @SerializedName("items")
    var items: ArrayList<SearchItem>? = null


}