package com.wnapp.trustmoney.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wnapp.trustmoney.data.model.BannerModel
import com.wnapp.trustmoney.data.model.CompanyDocModel
import com.wnapp.trustmoney.data.model.MoneyRequest
import com.wnapp.trustmoney.data.model.MoneyRequestResponse
import com.wnapp.trustmoney.data.model.Package
import com.wnapp.trustmoney.data.model.PaymentMethodItem
import com.wnapp.trustmoney.data.model.TransactionModel
import com.wnapp.trustmoney.data.model.TransactionType
import com.wnapp.trustmoney.data.model.WalletModel
import com.wnapp.trustmoney.data.repository.AuthRepository
import com.wnapp.trustmoney.data.repository.TransactionRepository
import com.wnapp.trustmoney.data.utils.NotificationHelper
import com.wnapp.trustmoney.utils.FileUtil
import kotlinx.coroutines.launch
import java.io.File
import com.wnapp.trustmoney.data.model.VerificationData
import kotlinx.coroutines.Dispatchers

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val tranRepo = TransactionRepository(application.applicationContext)
    var isLoading by mutableStateOf(false)
    private val _packages = mutableStateOf<List<Package>>(listOf())
    val packages: State<List<Package>> = _packages



    fun getPackages() {
        viewModelScope.launch {
            isLoading = true
            try {
                _packages.value = tranRepo.getPackages()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // এই ফাংশনটি এখন ক্লাসের ভেতরে নিশ্চিত করা হয়েছে


    private val _paymentMethods = mutableStateOf<List<PaymentMethodItem>>(listOf())
    val paymentMethods: State<List<PaymentMethodItem>> = _paymentMethods
    fun getPaymentMethods() {
        viewModelScope.launch {
            if(_paymentMethods.value.isEmpty()){
                isLoading = true
                try {
                    _paymentMethods.value = tranRepo.fetchPaymentMethods()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoading = false
                }
            }

        }
    }


    // ViewModel এর ভেতর
    var paymentMethod by mutableStateOf<PaymentMethodItem?>(null)
    fun getPaymentMethod(id: Int) {
        if (paymentMethod?.id == id) return

        viewModelScope.launch {
            isLoading = true
            try {
                val result = tranRepo.getPaymentMethod(id)
                if (result != null) {
                    paymentMethod = result
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

        //===========SubmitMoneyRequest=====================
    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    // AppViewModel.kt
    var isRequestSuccessful by mutableStateOf(false)
    fun submitMobileRequest(userId: Int, method: String, amount: String,  type: TransactionType, trxId: String, context: Context) {
        viewModelScope.launch {
            isLoading = true
            isRequestSuccessful = false
            val result = tranRepo.submitMoneyRequest(userId, method, amount,  type, trxId, null)
            if (result.isSuccess) {
                isRequestSuccessful = true // এটি ডায়ালগ ট্রিগার করবে

                val notificationHelper = com.wnapp.trustmoney.data.utils.NotificationHelper(context)
                notificationHelper.sendOtpNotification(
                    title = "Deposit Request Sent",
                    content = "আপনার $amount $method ডিপোজিট রিকোয়েস্টটি গ্রহণ করা হয়েছে।"
                )
            } else {
                // এরর হলে টোস্ট মেসেজ
                android.widget.Toast.makeText(context, "সার্ভার এরর! আবার চেষ্টা করুন।", android.widget.Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        }
    }

    // ব্যাংক পেমেন্টের জন্য (Image সহ)
    // ব্যাংক পেমেন্টের জন্য আপডেট করা ফাংশন
    fun submitBankRequest(
        userId: Int,
        method: String,
        amount: String,
        type: TransactionType,
        imageFile: File?,
        context: Context
    ) {
        viewModelScope.launch {
            isLoading = true
            isRequestSuccessful = false // রিকোয়েস্ট শুরুর আগে রিসেট করা

            val result = tranRepo.submitMoneyRequest(userId, method, amount, type,null, imageFile)

            if (result.isSuccess) {
                isRequestSuccessful = true // এটি UI-তে ডায়ালগ ট্রিগার করবে

                // সাকসেস নোটিফিকেশন পাঠানো
                val notificationHelper = NotificationHelper(context)
                notificationHelper.sendOtpNotification(
                    title = "Bank Deposit Request Sent",
                    content = "আপনার $amount $method ব্যাংক ডিপোজিট রিকোয়েস্টটি গ্রহণ করা হয়েছে।"
                )
            } else {
                // এরর হলে ইউজারকে জানানো
                android.widget.Toast.makeText(
                    context,
                    "ব্যাংক রিকোয়েস্ট ব্যর্থ হয়েছে! আবার চেষ্টা করুন।",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
            isLoading = false
        }
    }



    private fun handleResult(result: Result<MoneyRequestResponse>) {
        isLoading = false
        if (result.isSuccess) {
            dialogMessage = result.getOrNull()?.message ?: "Success"
        } else {
            dialogMessage = "Error: ${result.exceptionOrNull()?.message}"
        }
        showDialog = true
    }


    // ১. ওয়ালেটের একটি ইনিশিয়াল স্টেট তৈরি করুন
    var wallet by mutableStateOf<WalletModel?>(null)
    fun fetchWallet(userId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                if(wallet == null) {
                    val result = tranRepo.getWallet(userId)
                    wallet = result.body()
                }
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }



    var transactionList by mutableStateOf<List<TransactionModel>>(emptyList())
    fun fetchTransactionHistory(userId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                if (transactionList.isEmpty()) {
                    val response = tranRepo.getTransactions(userId)
                    if (response.isSuccessful) {
                        transactionList = response.body() ?: emptyList()
                    }
            }
                val response = tranRepo.getTransactions(userId)
                if (response.isSuccessful) {
                    transactionList = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }








}




    // ব্যানারের জন্য স্টেট
    var bannerList by mutableStateOf<List<BannerModel>>(emptyList())
    fun loadBanners() {
        viewModelScope.launch {
            try {
                if (bannerList.isEmpty()) {
                    val response = tranRepo.fetchBanners()
                    if (response.isSuccessful && response.body()?.success == true) {
                        bannerList = response.body()?.data?.filter { it.isActive } ?: emptyList()
                    }
                }
            } catch (e: Exception) {
            }
        }
    }



    var docList by mutableStateOf<List<CompanyDocModel>>(emptyList())
    fun loadDocs() {
        viewModelScope.launch {
            isLoading = true
            try {
                if (docList.isEmpty()){
                    val response = tranRepo.fetchCompanyDocs()
                    if (response.isSuccessful) {
                        docList = response.body() ?: emptyList()
                    }
                }
            } catch (e: Exception) {
                // এরর হ্যান্ডলিং
            } finally {
                isLoading = false
            }
        }
    }




        var requests by mutableStateOf<List<MoneyRequest>>(emptyList())
        fun loadMoneyRequestHistory(userId: Int) {
            viewModelScope.launch {
                isLoading = true
                val data = tranRepo.fetchMyRequests(userId)
                if (data != null) {
                    requests = data
                }else{
                    println("Faild to load data")
                }

                isLoading = false
            }
        }



    // In AppViewModel.kt
    var verificationStatus by mutableStateOf<String?>(null)

    fun submitVerification(
        userId: Int,
        docType: String,
        docNumber: String,
        frontUri: Uri,
        backUri: Uri?,
        context: Context
    ) {
        viewModelScope.launch {
            isLoading = true
            val frontFile = FileUtil.from(context, frontUri) // Helper to get File from Uri
            val backFile = backUri?.let { FileUtil.from(context, it) }

            val result = tranRepo.uploadDocuments(userId, docType, docNumber, frontFile, backFile)
            if (result?.success == true) {
                verificationStatus = "pending"
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
            }
            isLoading = false
        }
    }



    var verificationData by mutableStateOf<VerificationData?>(null)
    fun fetchVerificationStatus(userId: Int) {
        viewModelScope.launch {
            isLoading = true
            val result = tranRepo.getMyStatus(userId) // Endpoint: /verification/my-status/:userId
            if (result?.success == true) {
                // The API returns a list, so we take the first/latest one
                verificationData = result.data?.firstOrNull()
            }
            isLoading = false
        }
    }


    // State for the history list
    var verificationHistory by mutableStateOf<List<VerificationData>>(emptyList())


    // Optional: Function to refresh only the history
    fun refreshHistory(userId: Int) {
        viewModelScope.launch {
            verificationHistory = tranRepo.getVerificationHistory(userId)
        }
    }


    fun deleteRecord(id: Int, userId: Int, context: android.content.Context) {
        viewModelScope.launch {
            val success = tranRepo.deleteVerificationRecord(id)
            if (success) {
                // Refresh history and status after successful deletion
                refreshHistory(userId)
                fetchVerificationStatus(userId)
                android.widget.Toast.makeText(context, "Record deleted successfully", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                android.widget.Toast.makeText(context, "Failed to delete record", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

















// Factory ক্লাস (ক্রাশ রোধের জন্য)
class AppViewModelFactory(
    private val application: Application,
    private val authRepository: AuthRepository? = null // অপশনাল হিসেবে রাখুন
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> {
                AppViewModel(application) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                // AuthRepository ছাড়া AuthViewModel তৈরি করা সম্ভব নয়
                val repo = authRepository ?: AuthRepository(application.applicationContext)
                AuthViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
}