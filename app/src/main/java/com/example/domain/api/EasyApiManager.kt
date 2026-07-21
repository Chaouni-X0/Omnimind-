package com.example.omnimind.domain.api

import com.example.omnimind.data.apipool.ApiPoolManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class EasyApiManager {
    inline fun <reified T> createApiService(): T {
        return ApiPoolManager.create()
    }
    
    fun getBaseRetrofit(): Retrofit {
        return ApiPoolManager.retrofit
    }
    
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
