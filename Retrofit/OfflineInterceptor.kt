package com.example.api

import android.content.Context
import com.example.utils.isConnectedToNetwork
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class OfflineInterceptor(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.request()
        if (!context?.isConnectedToNetwork()) {
            val cacheControl = CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .build()

            response = response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .cacheControl(cacheControl)
                .build()

        }
       else {
            response = response.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        }
        return chain.proceed(response)
    }
}
