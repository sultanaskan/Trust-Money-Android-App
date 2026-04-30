package com.wnapp.trustmoney.data.model

data class LoginResponse(
    val token: String,

    // JSON-এ যেহেতু "user" কি (key) আছে, তাই এখানে 'user' ব্যবহার করা হয়েছে
    val user: User
)

data class User(
    val id: Int,
    val currencyId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val role: String,
    val status: String
) {
    // যদি আপনি আগের মতো fullName প্রপার্টি পেতে চান, তবে এটি ব্যবহার করতে পারেন
    val fullName: String
        get() = "$firstName $lastName"
}