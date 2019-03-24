package com.thunderhead.searchresults.datasources

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.thunderhead.searchresults.builders.SearchParamsBuilder
import com.thunderhead.searchresults.core.APIService
import com.thunderhead.searchresults.models.SearchItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchDataSource(private val service: APIService?, private val queryString: String, private val disposable: CompositeDisposable) : PageKeyedDataSource<Int, SearchItem>() {
    private val requestFailureLiveData: MutableLiveData<*>
    private var pageLimit = 1

    init {
        this.requestFailureLiveData = MutableLiveData<Any>()
    }


    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, SearchItem>) {
        pageLimit = params.requestedLoadSize
        val searchParamsBuilder = SearchParamsBuilder()
                .addLimit(pageLimit)
                .addTerm(queryString)
        service?.let { apiService ->
            this.disposable.add(apiService.getSearchResults(searchParamsBuilder.toParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ searchItems ->
                        callback.onResult(
                                searchItems, 0, searchItems.size, null, 2
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

        val searchParamsBuilder = SearchParamsBuilder()
                .addLimit(pageLimit)
                .addTerm(queryString)
        service?.let { apiService ->
            this.disposable.add(apiService.getSearchResults(searchParamsBuilder.toParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ searchItems ->
                        callback.onResult(
                                searchItems,
                                page + 1
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
