package com.wnapp.trustmoney.data.model


data class RegistrationFormData(
    val currencyId: Int = 0, // ডিফল্ট ভ্যালু যোগ করা হয়েছে
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val email: String = "",
    val role: String = "user",
    val status: String = "personal",
    val password: String = "",
    val confirmPassword: String = "",
    val dateOfBirth: String = ""
)