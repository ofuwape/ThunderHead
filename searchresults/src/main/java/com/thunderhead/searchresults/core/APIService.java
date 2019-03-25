package com.thunderhead.searchresults.core;

import com.thunderhead.searchresults.models.SearchItemContainer;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Custom Google Search API Service
 */
public interface APIService {


    @GET("customsearch/v1")
    Single<SearchItemContainer> getSearchResults(@QueryMap Map<String, String> params);

}