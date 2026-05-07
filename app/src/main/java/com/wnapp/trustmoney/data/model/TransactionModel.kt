package com.wnapp.trustmoney.data.model



data class TransactionModel(
    val id: Int,
    val transactionId: String,
    val userId: Int,
    val type: String, // 'deposit', 'withdraw', 'transfer', 'payment'
    val amount: Double,
    val status: String, // 'pending', 'success', 'failed'
    val description: String?,
    val createdAt: String
)