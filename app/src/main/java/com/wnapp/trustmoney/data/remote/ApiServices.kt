package com.wnapp.trustmoney.data.remote

import com.wnapp.trustmoney.data.model.LoginCreds
import com.wnapp.trustmoney.data.model.LoginResponse
import com.wnapp.trustmoney.data.model.RegistrationFormData
import com.wnapp.trustmoney.data.model.RegistrationResponse
import com.wnapp.trustmoney.data.model.CurrencyItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("user/register")
    suspend fun registerUser(@Body request: RegistrationFormData): Response<RegistrationResponse>

    @GET("currency")
    suspend fun getCurrency(): List<CurrencyItem>

    @GET("roles") // আপনার আসল এন্ডপয়েন্ট নাম দিন
    suspend fun getRoles(): List<String>

    @POST("user/login")
    suspend fun loginUser(@Body request: LoginCreds): Response<LoginResponse>
}