package com.wnapp.trustmoney.data.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.wnapp.trustmoney.MainActivity
import com.wnapp.trustmoney.R

class NotificationHelper(private val context: Context) {
    private val channelId = "OTP_CHANNEL"
    private val notificationId = 101

    fun sendOtpNotification(title: String, content: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // ১. নোটিফিকেশন চ্যানেল তৈরি (অ্যান্ড্রয়েড ও এবং তার উপরে)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Authentication OTP",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Used for receiving OTP and security alerts"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // ২. অ্যান্ড্রয়েড ১৩+ এর জন্য পারমিশন চেক
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // পারমিশন নেই, তাই নোটিফিকেশন পাঠানো যাবে না
                return
            }
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_trust_money_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }
}