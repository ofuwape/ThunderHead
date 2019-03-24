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

    @BindView(R2.id.search_title_view)
    var searchTitleView: TextView? = null

    @BindView(R2.id.search_desc_view)
    var searchDescriptionView: TextView? = null

    fun updateWithItem(searchItem: SearchItem) {
        searchTitleView?.text = searchItem.title
        searchDescriptionView?.text = searchItem.description
    }

}
