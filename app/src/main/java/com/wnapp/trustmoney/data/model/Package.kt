package com.wnapp.trustmoney.data.model

data class Package (
    val packageName: String,
    val price: Double,
    val validityDays: Int,
    val features: String
)