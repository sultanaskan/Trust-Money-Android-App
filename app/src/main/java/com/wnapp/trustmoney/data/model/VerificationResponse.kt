package com.wnapp.trustmoney.data.model

import com.google.gson.annotations.SerializedName

// Standard response wrapper for verification
data class VerificationResponse(
    val success: Boolean,
    val message: String?,
    val data: VerificationData?
)
data class StatusResponse(
    val success: Boolean,
    val message: String,
    val data: List<VerificationData>?
)
data class HistoryResponse(
    val success: Boolean,
    val data: List<VerificationData>
)

data class DeleteResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)

data class VerificationData(
    val id: Int,
    val userId: Int,
    val docType: String,
    val docNumber: String,
    val frontPartUrl: String,
    val backPartUrl: String?,
    val status: String, // 'pending', 'verified', 'rejected'
    val adminComment: String? = null
)