package com.wnapp.trustmoney.data.repository

import android.content.Context
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.FcmTokenRequest
import com.wnapp.trustmoney.data.model.FcmTokenResponse
import com.wnapp.trustmoney.data.model.MoneyRequest
import com.wnapp.trustmoney.data.model.MoneyRequestResponse
import com.wnapp.trustmoney.data.model.Package
import com.wnapp.trustmoney.data.model.PaymentMethodItem
import com.wnapp.trustmoney.data.model.StatusResponse
import com.wnapp.trustmoney.data.model.TransactionType
import com.wnapp.trustmoney.data.model.VerificationData
import com.wnapp.trustmoney.data.model.VerificationResponse
import com.wnapp.trustmoney.data.remote.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class TransactionRepository(context: Context) {
    private val api = RetrofitClient.apiService
    private val myCurrency = MyCurrency(context)

    suspend fun getPackages(): List<Package> = api.gePackages()

    suspend fun fetchPaymentMethods(): List<PaymentMethodItem> {
        return try {
            val response = api.getPaymentMethods()
            if (response.success) {
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getPaymentMethod(id: Int): PaymentMethodItem? {
        return try {
            val response = api.getPaymentMethod(id)
            if (response.success) {
                response.data // এখানে সরাসরি মেথড আইটেমটি রিটার্ন হবে
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCurrency(id: Int): CurrencyItem? {
        return try {
            val data = api.getCurrency(id)
            myCurrency.saveCurrency(data)
            data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // সংশোধিত ফাংশন
    suspend fun submitMoneyRequest(
        userId: Int,
        method: String,
        amount: String,
        type: TransactionType,
        trxId: String?,
        imageFile: File? = null
    ): Result<MoneyRequestResponse> {
        return try {
            // Plain text RequestBody তৈরি (টাইপিং মিস্টেক ঠিক করা হয়েছে)
            val uId = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val pMethod = method.toRequestBody("text/plain".toMediaTypeOrNull())
            val amt = amount.toRequestBody("text/plain".toMediaTypeOrNull())
            val typ = type.name.toRequestBody("text/plain".toMediaTypeOrNull())

            // trxId ঐচ্ছিক হতে পারে
            val tId = trxId?.toRequestBody("text/plain".toMediaTypeOrNull())

            var imagePart: MultipartBody.Part? = null
            imageFile?.let {
                if (it.exists()) {
                    val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                    imagePart = MultipartBody.Part.createFormData("recitImage", it.name, requestFile)
                }
            }

            // এপিআই কল
            val response = api.createMoneyRequest(uId, pMethod, amt, typ ,  tId, imagePart)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown Server Error"
                Result.failure(Exception("Error ${response.code()}: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
   suspend fun getNotifications(userId: Int) = api.getUserNotifications(userId)
   suspend fun markRead(id: Int) = api.markAsRead(id)
    suspend fun getWallet(userId: Int) = api.getWallet(userId)
    suspend fun getTransactions(userId: Int) = api.getUserTransactions(userId)

    suspend fun fetchBanners() = api.getBanners()

    suspend fun fetchCompanyDocs() = api.getAllDocs()


    // File: TransactionRepository.kt
    suspend fun fetchMyRequests(userId: Int): List<MoneyRequest>? {
        return try {
            val response = api.getMyRequests(userId)
            if (response.isSuccessful) {
                // response.body() is GetMoneyRequestResponse
                // .data is the List<MoneyRequest>
                response.body()?.data
            } else {
                null
            }
        } catch (e: Exception) {
            // Log the error to see if it's a connection issue
            println("Repository Error: ${e.message}")
            null
        }
    }



    // In VerificationRepository.kt
    suspend fun uploadDocuments(
        userId: Int,
        docType: String,
        docNumber: String,
        frontFile: File,
        backFile: File?
    ): VerificationResponse? {
        val uId = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val type = docType.toRequestBody("text/plain".toMediaTypeOrNull())
        val num = docNumber.toRequestBody("text/plain".toMediaTypeOrNull())

        val frontPart = MultipartBody.Part.createFormData(
            "frontPartImage", frontFile.name, frontFile.asRequestBody("image/*".toMediaTypeOrNull())
        )

        val backPart = backFile?.let {
            MultipartBody.Part.createFormData(
                "backPartImage", it.name, it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        val response = api.uploadVerification(uId, type, num, frontPart, backPart)
        return if (response.isSuccessful) response.body() else null
    }


    suspend fun getMyStatus(userId: Int): StatusResponse? {
        return try {
            val response = api.getMyStatus(userId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getVerificationHistory(userId: Int): List<VerificationData>{
        return try {
            val response = api.getVerificationHistory(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }

        }catch (e: Exception){
            emptyList()
        }
    }

    suspend fun deleteVerificationRecord(id: Int): Boolean {
        return try {
            val response = api.deleteVerification(id)
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            false
        }
    }




    suspend fun saveToken(userId: Int, token: String): Result<FcmTokenResponse> {
        return try {
            val response = api.saveFcmToken(FcmTokenRequest(userId, token))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to save token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}