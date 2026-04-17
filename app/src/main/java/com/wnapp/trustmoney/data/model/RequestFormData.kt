package com.wnapp.trustmoney.data.model


data class RequestFormData(
    val uniqueField: String = "", // এটি Account No / Credit Card / Prepaid Card হবে
    val email: String = "",
    val phone: String = "",
    val name: String = "",
    val securityQuestion: String = "",
    val securityAnswer: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)