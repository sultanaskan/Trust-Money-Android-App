package com.wnapp.trustmoney.data.model

import com.google.gson.annotations.SerializedName

// মূল রেসপন্স র‍্যাপার
data class PaymentMethodResponse(
    val success: Boolean,
    val count: Int,
    val data: List<PaymentMethodItem>
)

// প্রতিটি পেমেন্ট মেথডের ডিটেইলস
// ১. সিঙ্গেল পেমেন্ট মেথডের জন্য রেসপন্স মডেল
data class SinglePaymentMethodResponse(
    val success: Boolean,
    val data: PaymentMethodItem // এখানে List হবে না, সরাসরি Object হবে
)

// ২. প্রতিটি পেমেন্ট মেথডের ডিটেইলস (Nullable করা হয়েছে সেফটির জন্য)
data class PaymentMethodItem(
    val id: Int,
    val methodType: String?,
    val providerName: String?,
    val bankLogoUrl: String?,
    val accountNumber: String?,
    val accountType: String?,
    val paymentGuide: String?,
    val status: String?,
    val createdAt: String?,
    val updatedAt: String?
)