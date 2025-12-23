package com.example.practike3andr

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ActorsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            
            // Канал для уведомлений о парах
            val pairChannel = NotificationChannel(
                "pair_notification_channel",
                "Уведомления о парах",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о начале любимой пары"
                enableVibration(true)
                enableLights(true)
            }
            notificationManager.createNotificationChannel(pairChannel)
            
            // Тестовый канал
            val testChannel = NotificationChannel(
                "test_channel",
                "Тестовый канал",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Для тестирования уведомлений"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(testChannel)
        }
    }
}

