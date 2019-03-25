package com.thunderhead.searchresults.datasources

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.thunderhead.searchresults.R
import com.thunderhead.searchresults.builders.SearchParamsBuilder
import com.thunderhead.searchresults.core.APIService
import com.thunderhead.searchresults.core.SearchResults.Companion.DEFAULT_PAGE_SIZE
import com.thunderhead.searchresults.models.NetworkState
import com.thunderhead.searchresults.models.SearchItem
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class SearchDataSource(private val mContext: Context,
                       private val service: APIService?,
                       private val queryString: String,
                       private val disposable: CompositeDisposable) : PageKeyedDataSource<Int, SearchItem>() {

    private val requestFailureLiveData: MutableLiveData<*>
    private val searchApiKey = mContext.resources.getString(R.string.custom_search_key)
    private val searchCX = mContext.resources.getString(R.string.custom_search_cx)
    private var pageLimit: Int = DEFAULT_PAGE_SIZE

    /**
     * Keep Completable reference for the retry event
     */
    private var retryCompletable: Completable? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    init {
        this.requestFailureLiveData = MutableLiveData<Any>()
    }

    private fun getSearchParams(nextPageIndex: Int = 0): Map<String, String> {
        var numberOfResultsLeft = pageLimit
        if (nextPageIndex > 0) {
            numberOfResultsLeft = if (nextPageIndex < pageLimit && (pageLimit - nextPageIndex) < pageLimit) {
                pageLimit - nextPageIndex
            } else {
                0
            }
        }
        val searchParamsBuilder = SearchParamsBuilder()
                .addMaxResults(numberOfResultsLeft)
                .addQuery(queryString).addAPIKey(searchApiKey)
                .addCX(searchCX)
        if (nextPageIndex > 0) {
            searchParamsBuilder.addNextPageIndex(nextPageIndex)
        }
        return searchParamsBuilder.toParams()
    }

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, SearchItem>) {
        pageLimit = params.requestedLoadSize
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        service?.let { apiService ->
            this.disposable.add(apiService.getSearchResults(getSearchParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ searchContainer ->
                        searchContainer.items?.let { searchItems ->
                            setRetry(null)
                            networkState.postValue(NetworkState.LOADED)
                            initialLoad.postValue(NetworkState.LOADED)
                            callback.onResult(
                                    searchItems, 0,
                                    pageLimit,
                                    null, searchItems.size + 1)
                        }
                    }, {
                        setRetry(Action { loadInitial(params, callback) })
                        val error = NetworkState.error(it.message)
                        // publish the error
                        networkState.postValue(error)
                        initialLoad.postValue(error)
                    }))
        }
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, SearchItem>) {
        // This is not necessary in our case as our data doesn't change. It's useful in cases where
        // the data changes and we need to fetch our list starting from the middle.
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, SearchItem>) {

        val page = params.key
        if (page > pageLimit) {
            return
        }
        networkState.postValue(NetworkState.LOADING)

        service?.let { apiService ->
            this.disposable.add(apiService.getSearchResults(getSearchParams(page))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ searchContainer ->
                        searchContainer.items?.let { searchItems ->
                            setRetry(null)
                            networkState.postValue(NetworkState.LOADED)
                            callback.onResult(
                                    searchItems,
                                    page + searchItems.size + 1)
                        }
                    }, {
                        setRetry(Action { loadAfter(params, callback) })
                        networkState.postValue(NetworkState.error(it.message))
                    }))

        }

    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    fun retry() {
        if (retryCompletable != null) {
            disposable.add(retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }, { throwable ->
                        Toast.makeText(mContext, throwable.message, Toast.LENGTH_SHORT).show()
                    }))
        }
    }
}

