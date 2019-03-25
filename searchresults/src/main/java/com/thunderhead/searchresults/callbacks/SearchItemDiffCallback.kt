package com.thunderhead.searchresults.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.thunderhead.searchresults.models.SearchItem

class SearchItemDiffCallback : DiffUtil.ItemCallback<SearchItem>() {

    override fun areItemsTheSame(var1: SearchItem, var2: SearchItem): Boolean {
        return var1.cacheId == var2.cacheId
    }

    override fun areContentsTheSame(var1: SearchItem, var2: SearchItem): Boolean {
        return var1.description == var2.description
    }

}