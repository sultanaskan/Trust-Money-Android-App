package com.wnapp.trustmoney.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.Package
import com.wnapp.trustmoney.data.model.PaymentMethodItem
import com.wnapp.trustmoney.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val tranRepo = TransactionRepository(application.applicationContext)

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _packages = mutableStateOf<List<Package>>(listOf())
    val packages: State<List<Package>> = _packages

    private val _paymentMethods = mutableStateOf<List<PaymentMethodItem>>(listOf())
    val paymentMethods: State<List<PaymentMethodItem>> = _paymentMethods

    private val _currency = mutableStateOf<CurrencyItem?>(null)
    val currency: State<CurrencyItem?> = _currency

    fun getPackages() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _packages.value = tranRepo.getPackages()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // এই ফাংশনটি এখন ক্লাসের ভেতরে নিশ্চিত করা হয়েছে
    fun getPaymentMethods() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _paymentMethods.value = tranRepo.fetchPaymentMethods()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCurrencyById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _currency.value = tranRepo.getCurrency(id)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Factory ক্লাস (ক্রাশ রোধের জন্য)
class AppViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}