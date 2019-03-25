package com.thunderhead.searchresults.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.thunderhead.searchresults.R
import com.thunderhead.searchresults.R2
import com.thunderhead.searchresults.models.SearchItem

/**
 * ViewHolder to handle rendering of search text in view
 */
class SearchItemViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {

    @BindView(R2.id.search_title_view)
    var searchTitleView: TextView? = null

    @BindView(R2.id.search_desc_view)
    var searchDescriptionView: TextView? = null

    fun updateWithItem(searchItem: SearchItem) {
        if (searchTitleView == null) {
            searchTitleView = mItemView.findViewById(R.id.search_title_view)
        }
        if (searchDescriptionView == null) {
            searchDescriptionView = mItemView.findViewById(R.id.search_desc_view)
        }

        searchTitleView?.text = ""
        searchDescriptionView?.text = ""

        if (searchItem.title != null && searchItem.description != null) {
            searchTitleView?.text = searchItem.title?.trim()
            searchDescriptionView?.text = searchItem.description?.trim()

        }
    }

}
