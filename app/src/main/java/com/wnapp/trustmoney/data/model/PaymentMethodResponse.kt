package com.wnapp.trustmoney.data.model

import com.google.gson.annotations.SerializedName

// মূল রেসপন্স র‍্যাপার
data class PaymentMethodResponse(
    val success: Boolean,
    val count: Int,
    val data: List<PaymentMethodItem>
)

// প্রতিটি পেমেন্ট মেথডের ডিটেইলস
data class PaymentMethodItem(
    val id: Int,
    val methodType: String,      // e.g., mobile, bank
    val providerName: String,    // e.g., Bkash, Nagad, Trust Bank (আগে methodName ছিল)
    val bankLogoUrl: String?,    // লোগোর অনলাইন URL (nullable রাখা হয়েছে যদি লোগো না থাকে)
    val accountNumber: String,
    val accountType: String,     // e.g., personal, agent, saving
    val paymentGuide: String,
    val status: String,          // e.g., active, inactive
    val createdAt: String,
    val updatedAt: String
)