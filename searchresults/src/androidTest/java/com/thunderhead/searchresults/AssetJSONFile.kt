package com.thunderhead.searchresults

import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.InputStream

object AssetJSONFile {

    fun readByFilename(filename: String,
                       mContext: Context): String {
        var formArray = ByteArray(0)
        var file: InputStream? = null
        val tagName = "AssetJSONFile"
        val errTagName = "Error: "
        try {
            val manager = mContext.assets
            file = manager.open(filename)
            formArray = ByteArray(file!!.available())
            val count = file.read(formArray)
            Log.d(tagName, "byte counter: $count")
        } catch (e: IOException) {
            Log.e(tagName, String.format("%s%s", errTagName, e))
        } finally {
            try {
                file?.close()
            } catch (e: IOException) {
                Log.e(tagName, String.format("%s%s", errTagName, e))
            }

        }
        return String(formArray)
    }
}
