package com.thunderhead.searchresults.core

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.thunderhead.searchresults.R2
import com.thunderhead.searchresults.adapters.SearchItemsAdapter
import com.thunderhead.searchresults.models.SearchItem
import com.thunderhead.searchresults.viewmodels.SearchResultsViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class SearchResults @JvmOverloads
constructor(private var mContext: Context, var attrs: AttributeSet? = null)
    : FrameLayout(mContext, attrs), LifecycleOwner {

    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    @BindView(R2.id.search_recycler_view)
    var searchRecylerView: RecyclerView? = null

    private var mAPI: APIService? = null
    private val compositeDisposable = CompositeDisposable()
    private val mComponent: Component
    private var searchVieModel: SearchResultsViewModel? = null
    private var mainHandler = Handler(context.mainLooper)
    private var maxResults: Int = 12

    @Inject
    fun setConstructorParams(mAPI: APIService) {
        this.mAPI = mAPI
    }

    init {
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
        val view = View.inflate(mContext, com.thunderhead.searchresults.R.layout.search_results_view, this)
        lifecycleRegistry.markState(Lifecycle.State.STARTED)

        mComponent = Component.Initializer.init(false, mContext)
        mComponent.inject(this)
        ButterKnife.bind(this)
        if (searchRecylerView == null) {
            val recyclerView: RecyclerView? = view.findViewById(com.thunderhead.searchresults.R.id.search_recycler_view)
            searchRecylerView = recyclerView
        }
        retrieveMaxResults()
        configureSearchData()
    }

    private fun retrieveMaxResults() {
        maxResults = (attrs?.getAttributeIntValue("http://schemas.android.com/apk/res-auto",
                "max_search_results", 12)) ?: 12
    }

    private fun configureSearchData() {

        searchVieModel = SearchResultsViewModel(mAPI, "Thunderhead ONE",
                compositeDisposable, maxResults)

        val searchItemsAdapter = SearchItemsAdapter(mContext)
        searchRecylerView?.adapter = searchItemsAdapter

        //run on main thread
        mainHandler.post {
            searchVieModel?.getSearchItemLiveData()?.observe(this,
                    Observer<PagedList<SearchItem>> { value ->
                        value?.let {
                            searchItemsAdapter.submitList(value)
                        }
                    })
        }
    }

    private fun disposeComposable() {
        compositeDisposable.dispose()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

}
