package com.thunderhead.searchresults;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.thunderhead.searchresults.builders.SearchParamsBuilder;
import com.thunderhead.searchresults.core.APIService;
import com.thunderhead.searchresults.core.Component;
import com.thunderhead.searchresults.models.SearchItem;

import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchResultsView extends FrameLayout {

    @BindView(R2.id.search_recycler_view)
    RecyclerView searchRecylerView;

    protected APIService mAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Component mComponent;

    @Inject
    public void setConstructorParams(APIService mAPI) {
        this.mAPI = mAPI;
    }

    public SearchResultsView(Context context) {
        this(context, null);
    }

    public SearchResultsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.search_results_view, this);
        mComponent = Component.Initializer.init(false, context);
        mComponent.inject(this);

        ButterKnife.bind(this);
        fetchGoogleResults();
    }

    private void fetchGoogleResults() {
        if (mAPI != null) {
            compositeDisposable.add(mAPI.getSearchResults(new SearchParamsBuilder().toParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSearchSuccess, throwable -> onSearchFailure()));
        }
    }

    private void onSearchSuccess(List<SearchItem> searchItems) {

    }

    private void onSearchFailure() {

    }

    private void disposeComposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }


}
