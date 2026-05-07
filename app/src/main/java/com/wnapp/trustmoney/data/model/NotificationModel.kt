package com.wnapp.trustmoney.data.model

data class NotificationResponse(
    val success: Boolean,
    val count: Int? = null,
    val data: List<NotificationModel>
)

data class NotificationModel(
    val id: Int,
    val userId: Int?,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String
)