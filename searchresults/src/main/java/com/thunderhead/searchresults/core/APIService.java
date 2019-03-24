package com.thunderhead.searchresults.core;

import com.thunderhead.searchresults.models.SearchItem;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Curiosity API Service
 */
public interface APIService {


    @GET("plans/")
    Single<List<SearchItem>> getSearchResults(@QueryMap Map<String, String> params);

}