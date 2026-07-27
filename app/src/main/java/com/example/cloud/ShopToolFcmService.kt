package com.example.cloud

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ShopToolFcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM Registration Token: $token")
        // Token can be synced to server if needed
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "FCM Message Received from: ${remoteMessage.from}")

        val title = remoteMessage.notification?.title 
            ?: remoteMessage.data["title"] 
            ?: "ShopTool AI Notification"

        val message = remoteMessage.notification?.body 
            ?: remoteMessage.data["message"] 
            ?: remoteMessage.data["body"]
            ?: "You have a new update!"

        val category = remoteMessage.data["category"] ?: "announcement"

        showNotification(title, message, category)
    }

    private fun showNotification(title: String, message: String, category: String) {
        val channelId = getChannelIdForCategory(category)
        val channelName = getChannelNameForCategory(category)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "ShopTool AI Notifications for $channelName"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("notification_category", category)
            putExtra("notification_title", title)
            putExtra("notification_message", message)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private fun getChannelIdForCategory(category: String): String {
        return when (category.lowercase()) {
            "festival_wishes", "festival" -> "shoptool_festival_channel"
            "new_tool", "tool" -> "shoptool_tools_channel"
            "new_course", "course" -> "shoptool_courses_channel"
            "maintenance" -> "shoptool_maintenance_channel"
            "offer" -> "shoptool_offers_channel"
            "brand_collaboration", "brand" -> "shoptool_brand_channel"
            else -> "shoptool_announcements_channel"
        }
    }

    private fun getChannelNameForCategory(category: String): String {
        return when (category.lowercase()) {
            "festival_wishes", "festival" -> "Festival Wishes"
            "new_tool", "tool" -> "New Tools & Updates"
            "new_course", "course" -> "Creator & Video Courses"
            "maintenance" -> "Maintenance Updates"
            "offer" -> "Special Offers"
            "brand_collaboration", "brand" -> "Brand Collaborations"
            else -> "Announcements"
        }
    }

    companion object {
        private const val TAG = "ShopToolFcmService"
    }
}
