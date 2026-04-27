package com.wnapp.trustmoney.data.model

data class CurrencyItem(
    val id: Int,
    val countryName: String,
    val flagUrl: String,
    val currencyName: String,
    val rateInUsd: String
)