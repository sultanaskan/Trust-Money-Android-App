package com.wnapp.trustmoney.data.model

import com.google.gson.annotations.SerializedName

data class GetMoneyRequestResponse(
    val success: Boolean,
    val count: Int,
    val data: List<MoneyRequest>
)

data class MoneyRequest(
    val id: Int,
    val userId: Int,
    val type: String, // 'deposit', 'withdraw', 'recharge'
    @SerializedName("paymentMethod")
    val paymentMethod: String,
    val amount: String,
    @SerializedName("transactionId")
    val transactionId: String?,
    @SerializedName("recitUrl")
    val receiptUrl: String?,
    val status: String, // 'pending', 'approved', 'rejected'
    val createdAt: String,
    val updatedAt: String
)