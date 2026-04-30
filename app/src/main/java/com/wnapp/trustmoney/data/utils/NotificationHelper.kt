package com.wnapp.trustmoney.data.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.wnapp.trustmoney.R

class NotificationHelper(private val context: Context) {
    private val channelId = "OTP_CHANNEL"
    private  val  notificationId  = 101
    fun sendOtpNotification(otp: String){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Authentication OTP", NotificationManager.IMPORTANCE_HIGH )
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_trust_money_logo)
            .setContentTitle("Trust Money Security")
            .setContentText("Your Security OTP is: $otp. Do not share it.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())


    }
}