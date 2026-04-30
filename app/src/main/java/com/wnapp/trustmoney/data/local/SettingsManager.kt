package com.wnapp.trustmoney.data.local

import android.app.Activity
import android.content.Context
import java.util.Locale


// --- Logic to Update App Language & Restart ---
fun updateLocale(context: Context, localeCode: String) {
    saveLocale(context, localeCode)
    val locale = Locale(localeCode)

    val resources = context.resources
    val configuration = resources.configuration
    configuration.setLocale(locale)

    resources.updateConfiguration(configuration, resources.displayMetrics)
    if(context is Activity){
        context.recreate()
    }
}

// এটি কেবল কনফিগারেশন সেট করবে, রিস্টার্ট করবে না
fun applyInitialLocale(context: Context, localeCode: String) {
    val locale = Locale(localeCode)
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
fun saveLocale(context: Context, lang: String){
    val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    prefs.edit().putString("My_Lang", lang).apply()
}
fun getSavedLocale(context: Context):String{
    val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    return prefs.getString("My_Lang", "en")?: "en"
}