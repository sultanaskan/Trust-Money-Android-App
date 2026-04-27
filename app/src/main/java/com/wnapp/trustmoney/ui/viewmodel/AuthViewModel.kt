package com.wnapp.trustmoney.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.LoginCreds
import com.wnapp.trustmoney.data.model.LoginResponse
import com.wnapp.trustmoney.data.model.RegistrationFormData
import com.wnapp.trustmoney.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    // ১. কান্ট্রি লিস্টের জন্য আলাদা ভেরিয়েবল
    private val _countries = mutableStateOf<List<CurrencyItem>>(listOf())
    val countries: State<List<CurrencyItem>> = _countries

    // ২. রোল লিস্টের জন্য আলাদা ভেরিয়েবল
    private val _roles = mutableStateOf<List<String>>(listOf())
    val roles: State<List<String>> = _roles

    // ৩. লোডিং স্টেটের জন্য
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // কান্ট্রি ডাটা আনার ফাংশন
    fun getCurrency() {
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
    // রোল ডাটা আনার ফাংশন
    fun getRolesFromServer() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.fetchRoles()
                _roles.value = result // রোল ভেরিয়েবলে ডাটা সেভ হবে
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // রেজিস্ট্রেশনের রেজাল্ট ট্র্যাক করার জন্য

    // এই ফাংশনটি যোগ করুন
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


    var loginStatus = mutableStateOf<String?>(null)
    var loginResponse = mutableStateOf<LoginResponse?>(null)
    fun loginUser(creds: LoginCreds) {
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
                        println("USER: "+ loginResponse.value)
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