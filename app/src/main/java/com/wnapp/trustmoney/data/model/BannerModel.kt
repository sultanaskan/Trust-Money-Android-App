package com.wnapp.trustmoney.data.model

data class BannerResponse(
    val success: Boolean,
    val data: List<BannerModel>
)

data class BannerModel(
    val id: Int,
    val title: String,
    val bannerUrl: String,
    val isActive: Boolean
)