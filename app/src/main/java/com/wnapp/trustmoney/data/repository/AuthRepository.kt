package com.wnapp.trustmoney.data.repository

import android.content.Context
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.LoginCreds
import com.wnapp.trustmoney.data.model.LoginResponse
import com.wnapp.trustmoney.data.model.RegistrationFormData
import com.wnapp.trustmoney.data.remote.RetrofitClient
// retrofit2.Response ব্যবহার নিশ্চিত করুন
import retrofit2.Response

class AuthRepository(context: Context) { // Context এখানে পাস করুন
    private val api = RetrofitClient.apiService
    private val tranRepo = TransactionRepository(context)
    private val sessionManager = SessionManager(context) // একবারই ডিক্লেয়ার করুন

    suspend fun getCurrencyList(): List<CurrencyItem> = api.getCurrencies()

    suspend fun register(formData: RegistrationFormData) = api.registerUser(formData)

    suspend fun login(creds: LoginCreds): Response<LoginResponse> {
        val response = api.loginUser(creds)

        // লগইন সফল হলে ডাটা সেভ হবে
        if (response.isSuccessful && response.body() != null) {
            sessionManager.saveUser(response.body()!!)
            val currencyId = response.body()?.user?.currencyId ?: 1
           tranRepo.getCurrency(currencyId)
        }
        return response
    }



    suspend fun fetchRoles() = api.getRoles()
}