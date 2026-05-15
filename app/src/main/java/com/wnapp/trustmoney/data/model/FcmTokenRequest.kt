package com.wnapp.trustmoney.data.model

// Request Body
data class FcmTokenRequest(
    val userId: Int,
    val token: String,
    val platform: String = "android"
)

// Response Body
data class FcmTokenResponse(
    val success: Boolean,
    val message: String
)