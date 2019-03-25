package com.thunderhead.searchresults.core

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.thunderhead.searchresults.datasources.SearchDataSource
import com.thunderhead.searchresults.models.SearchItem
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory(private val mContext: Context,
                              private val service: APIService?,
                              private val queryString: String,
                              private val disposable: CompositeDisposable)
    : DataSource.Factory<Int, SearchItem>() {

    val searchDataSourceLiveData = MutableLiveData<SearchDataSource>()
    private var searchDataSource: SearchDataSource? = null

    override fun create(): DataSource<Int, SearchItem> {
        searchDataSource = SearchDataSource(mContext, service, queryString, disposable)
        searchDataSourceLiveData.postValue(searchDataSource)
        return searchDataSource!!
    }
}
