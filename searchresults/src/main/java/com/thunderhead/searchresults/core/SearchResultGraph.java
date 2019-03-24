package com.thunderhead.searchresults.core;

/**
 * Common interface implemented by both the release and debug flavored components
 */
public interface SearchResultGraph {

    /*
    Common inject signatures for both release and debug variants
     */
    void inject(SearchResults searchResults);

}
