package com.nunu.android_dating_app.message.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nunu.android_dating_app.R

class FirebaseService : FirebaseMessagingService(){

    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    // 메세지 받는 함수
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title.toString()
        val body = message.notification?.body.toString()

        // notificationChannel 생성 함수 호출
        createNotificationChannel()

        // notification 보내는 함수 호출
        sendNotification(title, body)

    }

    // notificationChannel 생성 함수
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // notification message 보내는 함수
    private fun sendNotification(title : String, body: String){

        var builder = NotificationCompat.Builder(this, "Test_Channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(123, builder.build())
        }

    }

}