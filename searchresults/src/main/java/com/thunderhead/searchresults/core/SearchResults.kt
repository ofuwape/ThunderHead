package com.thunderhead.searchresults.core

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.thunderhead.searchresults.R
import com.thunderhead.searchresults.adapters.SearchItemsAdapter
import com.thunderhead.searchresults.models.NetworkState
import com.thunderhead.searchresults.models.SearchItem
import com.thunderhead.searchresults.models.Status
import com.thunderhead.searchresults.viewmodels.SearchResultsViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class SearchResults @JvmOverloads
constructor(private var mContext: Context, private var attrs: AttributeSet? = null)
    : FrameLayout(mContext, attrs), LifecycleOwner {

    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    var searchRecylerView: RecyclerView? = null
    var swipeToRefresh: SwipeRefreshLayout? = null
    var retryButton: Button? = null
    var errorMessageView: TextView? = null
    var progressBar: ProgressBar? = null

    private var mAPI: APIService? = null
    private val compositeDisposable = CompositeDisposable()
    private val mComponent: Component
    private var searchVieModel: SearchResultsViewModel? = null
    private var mainHandler = Handler(context.mainLooper)
    private var maxResults: Int = DEFAULT_PAGE_SIZE

    var searchItemsAdapter: SearchItemsAdapter? = null

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
        setUpViews(view)
        retrieveMaxResults()
        configureSearchData()
    }

    private fun setUpViews(view: View) {
        if (searchRecylerView == null) {
            searchRecylerView = view.findViewById(com.thunderhead.searchresults.R.id.search_recycler_view)
        }
        if (swipeToRefresh == null) {
            swipeToRefresh = view.findViewById(com.thunderhead.searchresults.R.id.refresh_layout)
        }
        if (retryButton == null) {
            retryButton = view.findViewById(com.thunderhead.searchresults.R.id.retry_button)
        }
        if (errorMessageView == null) {
            errorMessageView = view.findViewById(com.thunderhead.searchresults.R.id.error_message_view)
        }
        if (progressBar == null) {
            progressBar = view.findViewById(com.thunderhead.searchresults.R.id.progress_bar)
        }
    }

    private fun retrieveMaxResults() {
        maxResults = (attrs?.getAttributeIntValue(mContext.getString(R.string.attribute_namespace),
                mContext.getString(R.string.max_result_key), DEFAULT_PAGE_SIZE))
                ?: DEFAULT_PAGE_SIZE
        if (maxResults < 1) {
            maxResults = DEFAULT_PAGE_SIZE
        }
    }

    /**
     * Configures ViewModel, DataSource and Adapter
     */
    private fun configureSearchData() {

        searchVieModel = SearchResultsViewModel(mContext, mAPI, mContext.getString(R.string.search_query),
                compositeDisposable, maxResults)
        searchItemsAdapter = SearchItemsAdapter(mContext)
        searchRecylerView?.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        searchRecylerView?.adapter = searchItemsAdapter

        mainHandler.post {
            searchVieModel?.getSearchItemLiveData()?.observe(this,
                    Observer<PagedList<SearchItem>> { value ->
                        value?.let {
                            searchItemsAdapter?.submitList(value)
                        }
                    })
        }
        initSwipeToRefresh()
    }

    private fun disposeComposable() {
        compositeDisposable.dispose()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    companion object {
        const val DEFAULT_PAGE_SIZE: Int = 12
    }

    // Handle Server Error UI States
    private fun initSwipeToRefresh() {
        mainHandler.post {
            searchVieModel?.getRefreshState()?.observe(this, Observer { networkState ->
                if (searchItemsAdapter?.currentList != null) {
                    if (searchItemsAdapter?.currentList!!.size > 0) {
                        swipeToRefresh?.isRefreshing = networkState?.status == NetworkState.LOADING.status
                    } else {
                        setInitialLoadingState(networkState)
                    }
                } else {
                    setInitialLoadingState(networkState)
                }
            })
        }
        swipeToRefresh?.setOnRefreshListener { searchVieModel?.refresh() }
    }

    private fun setInitialLoadingState(networkState: NetworkState?) {

        errorMessageView?.visibility = if (networkState?.message != null) View.VISIBLE else View.GONE
        if (networkState?.message != null) {
            errorMessageView?.text = networkState.message
        }


        retryButton?.visibility = if (networkState?.status === Status.FAILED) View.VISIBLE else View.GONE
        progressBar?.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE

        swipeToRefresh?.isEnabled = networkState?.status == Status.SUCCESS
        swipeToRefresh?.isRefreshing = !(networkState?.status == Status.SUCCESS)
        retryButton?.setOnClickListener { searchVieModel?.retry() }
    }

}
