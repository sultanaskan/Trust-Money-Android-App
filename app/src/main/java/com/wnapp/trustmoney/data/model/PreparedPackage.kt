package com.wnapp.trustmoney.data.model

data class PreparedPackage(
    val packageName: String,
    val packageFeature: String,
    val totalDeposit: String,
    val totalReturn: String,
    val todayRate: String,
    val currency: String,
)
