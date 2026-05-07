package com.wnapp.trustmoney.data.model

data class WalletModel(
    val id: Int,
    val userId: Int,
    val balance: String,
    val currency: String,
    val status: String
)