package com.thunderhead.searchresults

import android.content.Context
import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Helps to mock Google Custom Search API Response in testing enviroment.
 */
class MockWebServerUtil {

    enum class MockWebServerType constructor(i: Int) {
        GOOD(200),
        NOT_FOUND(404),
        UNAUTHORIZED(401)
    }

    companion object {

        @Singleton
        var currentServer: MockWebServer? = null

        private fun getServerInstance(): MockWebServer {
            if (currentServer == null) {
                currentServer = MockWebServer()
            }
            return currentServer!!
        }

        fun getServerUrl(): String {
            return getServerInstance().url("/").toString()
        }

        fun startMockServer(mContext: Context,
                            mockWebServerType: MockWebServerType): MockWebServer {
            currentServer = getServerInstance()
            MainUtil.mockServerUrl = getServerUrl()
            currentServer?.url(mContext.resources.getString(R.string.api_path))
            when (mockWebServerType) {
                MockWebServerUtil.MockWebServerType.UNAUTHORIZED, MockWebServerUtil.MockWebServerType.NOT_FOUND -> MockWebServerUtil.setBadServerDispatch(currentServer!!, mockWebServerType)
                MockWebServerUtil.MockWebServerType.GOOD -> MockWebServerUtil.setSuccessServerDispatch(mContext, currentServer!!)
                else -> MockWebServerUtil.setSuccessServerDispatch(mContext, currentServer!!)
            }
            try {
                currentServer?.start()
            } catch (e: Exception) {
                Log.e("Server Error", "Message: $e")
            }

            return currentServer!!
        }

        private fun setSuccessServerDispatch(mContext: Context,
                                             server: MockWebServer) {
            val dispatcher = object : Dispatcher() {

                @Throws(InterruptedException::class)
                override fun dispatch(request: RecordedRequest): MockResponse {
                    if (request.path.contains("customsearch/v1")) {
                        return MockResponse()
                                .setResponseCode(200)
                                .setBodyDelay(2, TimeUnit.SECONDS)
                                .setBody(AssetJSONFile.readByFilename("json/search_results.json", mContext))
                    }

                    return MockResponse().setResponseCode(404)
                }
            }

            server.setDispatcher(dispatcher)
        }

        private fun setBadServerDispatch(server: MockWebServer,
                                         mockWebServerType: MockWebServerType) {
            val dispatcher = object : Dispatcher() {
                @Throws(InterruptedException::class)
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return MockResponse().setResponseCode(if (mockWebServerType == MockWebServerType.UNAUTHORIZED)
                        404
                    else
                        401)
                }
            }
            server.setDispatcher(dispatcher)
        }

    }
}
