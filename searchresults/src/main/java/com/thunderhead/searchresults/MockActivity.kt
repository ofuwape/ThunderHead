package com.thunderhead.searchresults

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thunderhead.searchresults.core.SearchResults

class MockActivity : AppCompatActivity() {

    var searchResults: SearchResults? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        searchResults = findViewById(R.id.my_search_results)
    }

    companion object {
        var layout: Int = 0
    }
}