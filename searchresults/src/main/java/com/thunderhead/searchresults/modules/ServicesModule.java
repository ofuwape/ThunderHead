package com.thunderhead.searchresults.modules;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.thunderhead.searchresults.BuildConfig;
import com.thunderhead.searchresults.R;
import com.thunderhead.searchresults.core.APIService;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module returning mockable version of the API services
 */
@Module
public final class ServicesModule {

    private Context mContext;
    private boolean provideMocks;

    /**
     * Creates new instance of ServicesModule.
     *
     * @param provideMocks determine if API service should be mocked.
     * @param context      context calling method
     */
    public ServicesModule(boolean provideMocks, Context context) {
        this.mContext = context;
        this.provideMocks = provideMocks;
    }


    private Request.Builder setUpHeaders(@NonNull Request.Builder requestBuilder) {
        requestBuilder.addHeader("x-client-version", BuildConfig.VERSION_NAME);
        return requestBuilder;
    }

    /**
     * Provides new instance of APIService.
     *
     * @return APIService
     * @see APIService
     */
    @Provides
    @Singleton
    public APIService provideAPI() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {

                    Request original = chain.request();
                    Request.Builder originalRequestBuilder = original.newBuilder()
                            .header("Accept", "applicaton/json")
                            .method(original.method(), original.body());
                    return chain.proceed(originalRequestBuilder.build());
                }).build();

        String baseUrl = mContext.getResources().getString(R.string.api_path);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(APIService.class);
    }
}
