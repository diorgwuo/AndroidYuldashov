package com.example.practike3andr.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.practike3andr.MainActivity

class PairNotificationReceiver : BroadcastReceiver() {
    
    companion object {
        const val CHANNEL_ID = "pair_notification_channel"
        const val NOTIFICATION_ID = 1
        const val EXTRA_USER_NAME = "user_name"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        android.util.Log.d("PairNotificationReceiver", "onReceive вызван")
        val userName = intent.getStringExtra(EXTRA_USER_NAME) ?: "Пользователь"
        
        createNotificationChannel(context)
        showNotification(context, userName)
    }
    
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Уведомления о парах",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о начале любимой пары"
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun showNotification(context: Context, userName: String) {
        try {
            android.util.Log.d("PairNotificationReceiver", "Показ уведомления для: $userName")
            
            // Intent для открытия приложения при нажатии на уведомление
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Начало пары")
                .setContentText("$userName, начинается ваша любимая пара!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
            
            android.util.Log.d("PairNotificationReceiver", "Уведомление показано успешно")
        } catch (e: Exception) {
            android.util.Log.e("PairNotificationReceiver", "Ошибка при показе уведомления", e)
        }
    }
}

