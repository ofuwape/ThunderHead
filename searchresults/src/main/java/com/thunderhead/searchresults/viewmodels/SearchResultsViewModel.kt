package com.thunderhead.searchresults.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.thunderhead.searchresults.core.APIService
import com.thunderhead.searchresults.datasources.SearchDataSource
import com.thunderhead.searchresults.executor.MainThreadExecutor
import com.thunderhead.searchresults.models.SearchItem
import io.reactivex.disposables.CompositeDisposable


class SearchResultsViewModel constructor(apiService: APIService?, query: String,
                                         compositeDisposable: CompositeDisposable,
                                         pageSize: Int = 12)
    : ViewModel() {

    private var searchItemLiveData: MutableLiveData<PagedList<SearchItem>> = MutableLiveData()

    init {

        val dataSource = SearchDataSource(apiService, query, compositeDisposable)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize)
                .setEnablePlaceholders(true)
                .build()

        val executor = MainThreadExecutor()

        searchItemLiveData.postValue(PagedList.Builder(dataSource, config)
                .setFetchExecutor(executor)
                .setNotifyExecutor(executor)
                .build())
    }

    fun getSearchItemLiveData() = searchItemLiveData

}