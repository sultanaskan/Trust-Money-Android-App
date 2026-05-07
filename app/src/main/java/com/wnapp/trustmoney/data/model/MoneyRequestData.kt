package com.wnapp.trustmoney.data.model

data class MoneyRequestResponse(
    val success: Boolean,
    val message: String,
    val data: MoneyRequestData? = null
)

data class MoneyRequestData(
    val id: Int,
    val userId: Int,
    val paymentMethod: String,
    val type: TransactionType,
    val amount: String,
    val transactionId: String?,
    val status: String
)

enum class TransactionType {
    deposit,
    withdraw,
    recharge
}