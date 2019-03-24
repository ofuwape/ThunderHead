package com.thunderhead.searchresults.datasources

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.thunderhead.searchresults.R
import com.thunderhead.searchresults.builders.SearchParamsBuilder
import com.thunderhead.searchresults.core.APIService
import com.thunderhead.searchresults.core.SearchResults.Companion.DEFAULT_PAGE_SIZE
import com.thunderhead.searchresults.models.SearchItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchDataSource(mContext: Context,
                       private val service: APIService?,
                       private val queryString: String,
                       private val disposable: CompositeDisposable) : PageKeyedDataSource<Int, SearchItem>() {

    private val requestFailureLiveData: MutableLiveData<*>
    private val searchApiKey = mContext.resources.getString(R.string.custom_search_key)
    private val searchCX = mContext.resources.getString(R.string.custom_search_cx)
    private var pageLimit: Int = DEFAULT_PAGE_SIZE

    init {
        this.requestFailureLiveData = MutableLiveData<Any>()
    }

    private fun getSearchParams(nextPageIndex: Int = 0): Map<String, String> {
        val searchParamsBuilder = SearchParamsBuilder()
                .addMaxResults(pageLimit)
                .addQuery(queryString).addAPIKey(searchApiKey)
                .addCX(searchCX)
        if (nextPageIndex > 0) {
            searchParamsBuilder.addNextPageIndex(nextPageIndex)
        }
        return searchParamsBuilder.toParams()
    }

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, SearchItem>) {
        pageLimit = params.requestedLoadSize

        service?.let { apiService ->
            this.disposable.add(apiService.getSearchResults(getSearchParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ searchItems ->
                        callback.onResult(
                                searchItems, 0, searchItems.size, null, searchItems.size + 1
                        )
                    }, {
                        this.handleError(it)
                    }))
        }
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, SearchItem>) {
        // This is not necessary in our case as our data doesn't change. It's useful in cases where
        // the data changes and we need to fetch our list starting from the middle.
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, SearchItem>) {

        val page = params.key
        service?.let { apiService ->
            this.disposable.add(apiService.getSearchResults(getSearchParams(page))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ searchItems ->
                        callback.onResult(
                                searchItems,
                                page + searchItems.size + 1
                        )
                    }, {
                        this.handleError(it)
                    }))

        }

    }

    private fun handleError(t: Throwable) {
        //        requestFailureLiveData.postValue(t.getMessage());
    }
}
