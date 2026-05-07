package com.wnapp.trustmoney.data.local

import android.annotation.SuppressLint
import android.content.Context
import com.wnapp.trustmoney.data.model.LoginResponse

class SessionManager(context: Context){
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    fun saveUser(user: LoginResponse){
        prefs.edit().apply{
            putString("auth_token", user.token)
            putInt("currency_id", user.user.currencyId)
            putString("user_email", user.user.email)
            putString("user_fname", user.user.firstName)
            putString("user_lname", user.user.lastName)
            putInt("user_id", user.user.id)
            putString("user_role", user.user.role)
            putString("user_phone", user.user.phone)
            putString("user_status", user.user.status)
        }.apply()
    }
    fun savePin(pin: String){
        prefs.edit().apply{
            putString("user_pin", pin)
        }.apply()
    }
    fun saveProfileImageUri(uri: String) {
        prefs.edit().apply{
            putString("profile_uri", uri)
        }
    }
    fun getProfileImageUri(): String? =  prefs.getString("profile_uri", null)

    fun getPin(): String? = prefs.getString("user_pin",null)
    fun isPinSet(): Boolean {
        return getPin() != null
    }



    // --- GET Functions ---

    // টোকেন পাওয়ার জন্য
    fun getCurrencyId(): Int = prefs.getInt("currency_id",0)
    fun getToken(): String? = prefs.getString("auth_token", null)
    fun getEmail(): String? = prefs.getString("user_email", null)
    fun getFirstName(): String? = prefs.getString("user_fname", null)
    fun getLastName(): String? = prefs.getString("user_lname", null)
    fun getFullName(): String = getFirstName() + " "+ getLastName()
    fun getUserId(): Int = prefs.getInt("user_id", 0)
    fun getUserRole(): String? = prefs.getString("user_role", null)
    fun getUserPhone(): String? = prefs.getString("user_phone", null)
    fun getUserStatus(): String? = prefs.getString("user_status", null)
    // --- বোনাস: লগআউট ফাংশন ---
    // ইউজার লগআউট করলে সব ডাটা মুছে ফেলার জন্য এটি ব্যবহার করতে পারেন
    fun clearSession() {
        prefs.edit().clear().apply()
    }

    // ইউজার লগইন আছে কি না তা চেক করার জন্য
    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}