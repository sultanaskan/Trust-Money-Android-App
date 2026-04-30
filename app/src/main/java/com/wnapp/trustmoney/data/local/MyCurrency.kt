package com.wnapp.trustmoney.data.local


import android.annotation.SuppressLint
import android.content.Context
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.LoginResponse

class MyCurrency(context: Context){
    private val prefs = context.getSharedPreferences("my_currency", Context.MODE_PRIVATE)
    fun saveCurrency(currency: CurrencyItem){
        prefs.edit().apply{
             putInt("currency_id", currency.id)
            putString("country_name", currency.currencyName)
            putString("flag_url", currency.flagUrl)
            putString("currency_name", currency.currencyName)
            putString("rate_in_usd", currency.rateInUsd)
        }.apply()
    }

    fun getCurrencyId(): Int = prefs.getInt("currency_id", 0)
    fun getCountryName(): String? = prefs.getString("country_name", null)
    fun getFlagUrl():String? = prefs.getString("flag_url", null )
    fun getCurrencyName():String? = prefs.getString("currency_name", null)
    fun getRateInUsd(): String? = prefs.getString("rate_in_usd", null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }

}