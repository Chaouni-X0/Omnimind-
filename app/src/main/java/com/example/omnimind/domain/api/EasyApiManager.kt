package com.example.omnimind.domain.api

import com.example.omnimind.data.apipool.ApiPoolManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class EasyApiManager {
    private val apiPoolManager: ApiPoolManager = ApiPoolManager
    
    inline fun <reified T> createApiService(): T {
        return apiPoolManager.create()
    }
    
    fun getBaseRetrofit(): Retrofit {
        return apiPoolManager.retrofit
    }
    
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
