package com.wnapp.trustmoney.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.LoginCreds
import com.wnapp.trustmoney.data.model.LoginResponse
import com.wnapp.trustmoney.data.model.RegistrationFormData
import com.wnapp.trustmoney.data.repository.AuthRepository
import com.wnapp.trustmoney.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading


    //==============Currency or country list retrival ১. কান্ট্রি লিস্টের জন্য আলাদা ভেরিয়েবল
    private val _countries = mutableStateOf<List<CurrencyItem>>(listOf())
    val countries: State<List<CurrencyItem>> = _countries
    fun getCurrencies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // রিপোজিটরি থেকে শুধু নামের লিস্টটি নিচ্ছি
                val result = repository.getCurrencyList()
                _countries.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

  //================Registration process==============
    fun resetRegistrationStatus() {
        registrationStatus.value = null
    }
    var registrationStatus = mutableStateOf<String?>(null)
    fun registerUser(formData: RegistrationFormData) {
        if ((formData.currencyId != 0) && (formData.password != formData.confirmPassword)) {
            registrationStatus.value = "Passwords do not match! Or country May not Selected"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.register(formData)
                if (response.isSuccessful) {
                    registrationStatus.value = "Success"
                } else {
                    println("Registration failed: ${response}")
                    registrationStatus.value = "Failed: ${response.message()}"
                }
            } catch (e: Exception) {
                registrationStatus.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }





    //================Login Process===========
    var loginStatus = mutableStateOf<String?>(null)
    var loginResponse = mutableStateOf<LoginResponse?>(null)
    fun loginUser(creds: LoginCreds, onSuccess:() -> Unit) {
        if(creds.email.isEmpty() || creds.password.isEmpty()){
            loginStatus.value = "Please enter email and password"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
                try {
                    val response = repository.login(creds)
                    if(response.isSuccessful && response.body() != null){
                        loginResponse.value = response.body()
                        loginStatus.value = "Success"
                        onSuccess()
                    }
                } catch (e: Exception){
                    loginStatus.value = "Error (Invalid email or password): ${e.localizedMessage}"
                }finally {
                    _isLoading.value = false
                }

        }
    }
    fun resetLoginStatus(){
        loginStatus.value = null
    }
    fun logoutUser(context: Context): Boolean{
        val sm = SessionManager(context)
        sm.clearSession()
        return true
    }









}



class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}