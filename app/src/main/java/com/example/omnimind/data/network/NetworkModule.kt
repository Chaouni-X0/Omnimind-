package com.example.omnimind.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun provideOkHttpClient(): OkHttpClient = okHttpClient

    private fun retrofitFor(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val geminiApiService: GeminiApiService by lazy {
        retrofitFor("https://generativelanguage.googleapis.com/").create(GeminiApiService::class.java)
    }

    val gitHubApiService: GitHubApiService by lazy {
        retrofitFor("https://api.github.com/").create(GitHubApiService::class.java)
    }
}
