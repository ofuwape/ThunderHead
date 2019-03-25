package com.thunderhead.searchresults.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.thunderhead.searchresults.core.APIService
import com.thunderhead.searchresults.core.SearchDataSourceFactory
import com.thunderhead.searchresults.core.SearchResults.Companion.DEFAULT_PAGE_SIZE
import com.thunderhead.searchresults.datasources.SearchDataSource
import com.thunderhead.searchresults.models.NetworkState
import com.thunderhead.searchresults.models.SearchItem
import io.reactivex.disposables.CompositeDisposable


class SearchResultsViewModel constructor(mContext: Context,
                                         apiService: APIService?, query: String,
                                         var compositeDisposable: CompositeDisposable,
                                         var pageSize: Int = DEFAULT_PAGE_SIZE)
    : ViewModel() {

    private var searchItemLiveData: LiveData<PagedList<SearchItem>> = MutableLiveData()
    private var config: PagedList.Config = PagedList.Config.Builder()
            .setPageSize(pageSize / 2)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()
    private var sourceFactory: SearchDataSourceFactory? = null

    init {
        sourceFactory = SearchDataSourceFactory(mContext, apiService, query, compositeDisposable)
        searchItemLiveData = LivePagedListBuilder<Int, SearchItem>(sourceFactory!!, config).build()
    }


    fun getSearchItemLiveData() = searchItemLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun retry() {
        sourceFactory?.searchDataSourceLiveData?.value!!.retry()
    }

    fun refresh() {
        sourceFactory?.searchDataSourceLiveData?.value!!.invalidate()
    }

    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<SearchDataSource, NetworkState>(
            sourceFactory?.searchDataSourceLiveData!!) { it.networkState }

    fun getRefreshState(): LiveData<NetworkState> = Transformations.switchMap<SearchDataSource, NetworkState>(
            sourceFactory?.searchDataSourceLiveData!!) { it.initialLoad }

}