package com.wnapp.trustmoney.data.repository

import android.content.Context
import android.widget.Toast
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.Package
import com.wnapp.trustmoney.data.model.PaymentMethodItem
import com.wnapp.trustmoney.data.remote.RetrofitClient
import retrofit2.Response
import kotlin.math.cos


class TransactionRepository(context: Context) {
    private val api = RetrofitClient.apiService
    private val myCurrency = MyCurrency(context)

    suspend fun  getPackages(): List<Package> = api.gePackages();
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

    // ২. এই ফাংশনটি ভালো করে লক্ষ্য করুন
    suspend fun getCurrency(id: Int): CurrencyItem? {
        return try {
            // সরাসরি এপিআই কল (রেসপন্স র‍্যাপার ছাড়া)
            val data = api.getCurrency(id)

            // এখানে ডাটা আসা মানেই কলটি সফল হয়েছে (নয়তো সরাসরি catch-এ চলে যেত)
            myCurrency.saveCurrency(data)


            data // ডেটা রিটার্ন করুন
        } catch (e: Exception) {
            // নেটওয়ার্ক এরর, ৪মে বা ৫০০ এরর সব এখানেই ধরা পড়বে
            e.printStackTrace()
            null
        }
    }
}