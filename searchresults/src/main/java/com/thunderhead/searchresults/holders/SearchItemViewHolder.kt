package com.thunderhead.searchresults.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.thunderhead.searchresults.R2
import com.thunderhead.searchresults.models.SearchItem

/**
 * ViewHolder to handle rendering of search text in view
 */
class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @BindView(R2.id.search_text_view)
    var searchTextView: TextView? = null

    fun updateWithItem(searchItem: SearchItem) {
        searchTextView?.text = searchItem.text
    }

}
