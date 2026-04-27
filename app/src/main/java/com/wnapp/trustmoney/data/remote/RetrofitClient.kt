package com.wnapp.trustmoney.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://trustmoneyapi.wellcometoserbia.com/api/" // আপনার সার্ভার ইউআরএল দিন

    // ইন্টারসেপ্টর (এটি লগক্যাটে দেখাবে API তে কি ডাটা যাচ্ছে আর আসছে)
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create()) // JSON to Object conversion
            .build()
            .create(ApiService::class.java)
    }
}
