package com.thunderhead.searchresults.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.thunderhead.searchresults.R
import com.thunderhead.searchresults.callbacks.SearchItemDiffCallback
import com.thunderhead.searchresults.holders.SearchItemViewHolder
import com.thunderhead.searchresults.models.SearchItem

class SearchItemsAdapter(val mContext: Context) : PagedListAdapter<SearchItem,
        SearchItemViewHolder>(SearchItemDiffCallback()) {

    override fun onBindViewHolder(searchItemViewHolder: SearchItemViewHolder, position: Int) {
        val searchItem = getItem(position)
        searchItemViewHolder.updateWithItem(searchItem ?: SearchItem())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.search_item_view, parent, false)
        return SearchItemViewHolder(view)
    }

}