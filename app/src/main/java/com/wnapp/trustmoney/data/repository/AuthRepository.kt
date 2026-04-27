package com.wnapp.trustmoney.data.repository

import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.LoginCreds
import com.wnapp.trustmoney.data.model.RegistrationFormData
import com.wnapp.trustmoney.data.remote.RetrofitClient

class AuthRepository {
    private val api = RetrofitClient.apiService
    suspend fun getCurrencyList(): List<CurrencyItem> = api.getCurrency()
    // অথবা যদি শুধু নামের লিস্ট চান:


    suspend fun register(formData: RegistrationFormData) = api.registerUser(formData)
    suspend fun login(creds: LoginCreds) = api.loginUser(creds)
    suspend fun fetchRoles() = api.getRoles()
}