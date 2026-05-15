package com.wnapp.trustmoney.service

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wnapp.trustmoney.MainActivity
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.remote.RetrofitClient
import com.wnapp.trustmoney.data.repository.TransactionRepository
import com.wnapp.trustmoney.data.utils.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseService : FirebaseMessagingService() {

    // Called when a new token is generated (e.g., first install)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        val sessionManager = SessionManager(this) // আপনার SessionManager
        val userId = sessionManager.getUserId() // এটি হয়তো ০ বা empty স্ট্রিং দেয়

        // যদি ইউজার লগইন না থাকে, তবে এপিআই কল করার দরকার নেই
        if (userId == 0 || userId == null) {
            Log.d("FCM_SERVICE", "User not logged in. Skipping token sync.")
            return
        }

        val repository = TransactionRepository(this)
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.saveToken(userId, token)
            if (result.isSuccess) {
                Log.d("FCM_SERVICE", "Token saved successfully")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            // আপনার NotificationHelper ক্লাসটি ব্যবহার করুন
            val notificationHelper = NotificationHelper(this)
            notificationHelper.sendOtpNotification(
                it.title ?: "Notification",
                it.body ?: ""
            )
        }
    }


}

