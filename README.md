# Project Description

## Requirements
Provide a configurable amount of top google results for “Thunderhead ONE” as a widget that
can be added easily to an Android app.
The solution is an AAR library.

A consuming app should be able to use your widget in a layout:

```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:orientation="vertical" >

  <com.thunderhead.searchresults
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:max_search_results=”5” />
  
</LinearLayout>
```
