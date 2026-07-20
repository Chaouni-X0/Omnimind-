package com.example.omnimind.data.apipool

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiPoolManager {
    private const val BASE_URL = "https://api.example.com/"
    private const val TIMEOUT_SECONDS = 30L

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    inline fun <reified T> create(): T {
        return retrofit.create(T::class.java)
    }

    // Clean up resources
    fun shutdown() {
        okHttpClient.connectionPool().evictAll()
        okHttpClient.cache()?.close()
    }
}