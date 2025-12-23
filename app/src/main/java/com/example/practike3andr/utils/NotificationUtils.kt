package com.example.practike3andr.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.practike3andr.receiver.PairNotificationReceiver
import java.util.Calendar

object NotificationUtils {
    
    private const val REQUEST_CODE = 1001
    private const val TAG = "NotificationUtils"
    
    /**
     * Тестирует уведомление немедленно (для отладки)
     */
    fun testNotificationNow(context: Context, userName: String) {
        Log.d(TAG, "=== ТЕСТ УВЕДОМЛЕНИЯ ===")
        Log.d(TAG, "Пользователь: $userName")
        Log.d(TAG, "Android версия: ${Build.VERSION.SDK_INT}")
        
        // Проверяем разрешения
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val areEnabled = notificationManager.areNotificationsEnabled()
            Log.d(TAG, "Уведомления разрешены: $areEnabled")
            
            if (!areEnabled) {
                Log.e(TAG, "✗ УВЕДОМЛЕНИЯ ОТКЛЮЧЕНЫ В НАСТРОЙКАХ!")
                return
            }
        }
        
        // Создаем канал уведомлений
        createNotificationChannel(context)
        
        // Показываем уведомление напрямую, без BroadcastReceiver
        try {
            val intent = Intent(context, com.example.practike3andr.MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            // Используем иконку приложения вместо системной
            val iconRes = context.applicationInfo.icon
            Log.d(TAG, "Иконка приложения: $iconRes")
            
            val notification = androidx.core.app.NotificationCompat.Builder(context, PairNotificationReceiver.CHANNEL_ID)
                .setSmallIcon(if (iconRes != 0) iconRes else android.R.drawable.ic_dialog_info)
                .setContentTitle("Начало пары (ТЕСТ)")
                .setContentText("$userName, начинается ваша любимая пара!")
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .setDefaults(androidx.core.app.NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(androidx.core.app.NotificationCompat.CATEGORY_REMINDER)
                .build()
            
            notificationManager.notify(999, notification) // Используем другой ID для теста
            Log.d(TAG, "✓ Тестовое уведомление показано напрямую, ID: 999")
            
            // Проверяем каналы
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = notificationManager.getNotificationChannel(PairNotificationReceiver.CHANNEL_ID)
                Log.d(TAG, "Канал существует: ${channel != null}")
                if (channel != null) {
                    Log.d(TAG, "Важность канала: ${channel.importance}")
                }
            }
            
            // Также отправляем broadcast для проверки
            val broadcastIntent = Intent(context, PairNotificationReceiver::class.java).apply {
                putExtra(PairNotificationReceiver.EXTRA_USER_NAME, userName)
            }
            context.sendBroadcast(broadcastIntent)
            Log.d(TAG, "✓ Broadcast также отправлен")
        } catch (e: Exception) {
            Log.e(TAG, "✗ Ошибка при показе тестового уведомления", e)
            e.printStackTrace()
        }
    }
    
    /**
     * Устанавливает уведомление на указанное время
     * @param context Контекст приложения
     * @param time Время в формате HH:mm
     * @param userName Имя пользователя для уведомления
     */
    fun schedulePairNotification(context: Context, time: String, userName: String) {
        Log.d(TAG, "=== НАЧАЛО УСТАНОВКИ УВЕДОМЛЕНИЯ ===")
        Log.d(TAG, "Время: $time, Пользователь: $userName")
        
        try {
            val (hour, minute) = parseTime(time)
            Log.d(TAG, "Парсинг времени успешен: час=$hour, минута=$minute")
            
            val calendar = Calendar.getInstance().apply {
                val currentTime = timeInMillis
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                
                Log.d(TAG, "Текущее время системы: ${System.currentTimeMillis()}")
                Log.d(TAG, "Время уведомления до проверки: $timeInMillis")
                
                // Если время уже прошло сегодня, устанавливаем на завтра
                if (timeInMillis <= System.currentTimeMillis()) {
                    Log.d(TAG, "Время уже прошло, устанавливаем на завтра")
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            
            Log.d(TAG, "Установка уведомления на: ${calendar.time}, для пользователя: $userName")
            Log.d(TAG, "Время в миллисекундах: ${calendar.timeInMillis}")
            
            val intent = Intent(context, PairNotificationReceiver::class.java).apply {
                putExtra(PairNotificationReceiver.EXTRA_USER_NAME, userName)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Log.d(TAG, "AlarmManager получен")
            
            // Создаем канал уведомлений заранее
            createNotificationChannel(context)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12+ требует проверки разрешения
                val canScheduleExact = alarmManager.canScheduleExactAlarms()
                Log.d(TAG, "canScheduleExactAlarms() = $canScheduleExact")
                
                if (canScheduleExact) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                    Log.d(TAG, "✓ Уведомление установлено успешно (Android 12+, точное)")
                } else {
                    Log.w(TAG, "⚠ Нет разрешения на точные уведомления, используем неточное")
                    // Пробуем установить неточное уведомление
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                    Log.d(TAG, "✓ Установлено неточное уведомление")
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Log.d(TAG, "✓ Уведомление установлено успешно (Android 6+)")
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Log.d(TAG, "✓ Уведомление установлено успешно (старая версия)")
            }
            
            Log.d(TAG, "=== УСТАНОВКА УВЕДОМЛЕНИЯ ЗАВЕРШЕНА ===")
        } catch (e: Exception) {
            Log.e(TAG, "✗ ОШИБКА при установке уведомления", e)
            e.printStackTrace()
        }
    }
    
    /**
     * Создает канал уведомлений
     */
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                PairNotificationReceiver.CHANNEL_ID,
                "Уведомления о парах",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о начале любимой пары"
                enableVibration(true)
                enableLights(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "Канал уведомлений создан")
        }
    }
    
    /**
     * Отменяет установленное уведомление
     */
    fun cancelPairNotification(context: Context) {
        val intent = Intent(context, PairNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
    
    /**
     * Парсит время из строки формата HH:mm
     * @return Pair(hour, minute)
     */
    private fun parseTime(time: String): Pair<Int, Int> {
        val parts = time.split(":")
        if (parts.size != 2) {
            throw IllegalArgumentException("Некорректный формат времени")
        }
        
        val hour = parts[0].toIntOrNull()
            ?: throw IllegalArgumentException("Некорректный формат времени")
        val minute = parts[1].toIntOrNull()
            ?: throw IllegalArgumentException("Некорректный формат времени")
        
        if (hour !in 0..23 || minute !in 0..59) {
            throw IllegalArgumentException("Некорректный формат времени")
        }
        
        return Pair(hour, minute)
    }
    
    /**
     * Валидирует формат времени HH:mm
     */
    fun isValidTimeFormat(time: String): Boolean {
        return try {
            parseTime(time)
            true
        } catch (e: Exception) {
            false
        }
    }
}

