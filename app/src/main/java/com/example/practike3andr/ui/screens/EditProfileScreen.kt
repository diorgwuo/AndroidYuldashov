package com.example.practike3andr.ui.screens

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.practike3andr.data.model.UserProfile
import com.example.practike3andr.ui.viewmodel.ProfileViewModel
import com.example.practike3andr.utils.NotificationUtils
import java.io.File
import java.util.Calendar
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current
    
    var fullName by remember { mutableStateOf(profile.fullName) }
    var position by remember { mutableStateOf(profile.position) }
    var resumeUrl by remember { mutableStateOf(profile.resumeUrl) }
    var avatarUri by remember { mutableStateOf(profile.avatarUri) }
    var favoritePairTime by remember { mutableStateOf(profile.favoritePairTime) }
    var timeError by remember { mutableStateOf<String?>(null) }
    
    // Для TimePicker
    val calendar = remember { Calendar.getInstance() }
    
    // Временный файл для фото с камеры
    val tempImageFile = remember {
        val imageFileName = "temp_avatar_${System.currentTimeMillis()}.jpg"
        File(context.filesDir, imageFileName)
    }
    
    val tempImageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }
    
    // Launcher для выбора изображения из галереи
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatarUri = it.toString()
        }
    }
    
    // Launcher для камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            avatarUri = tempImageUri.toString()
        }
    }
    
    // Launcher для запроса разрешения на камеру
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(tempImageUri)
        }
    }
    
    // Launcher для запроса разрешения на уведомления (Android 13+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            android.util.Log.d("EditProfileScreen", "Разрешение на уведомления получено")
        } else {
            android.util.Log.w("EditProfileScreen", "Разрешение на уведомления отклонено")
            Toast.makeText(
                context,
                "Для работы уведомлений необходимо разрешение",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    // Диалог выбора источника фото
    var showImageSourceDialog by remember { mutableStateOf(false) }
    
    fun openGallery() {
        // GetContent не требует разрешений на Android 13+
        // Для старых версий Android система сама запросит разрешение при необходимости
        galleryLauncher.launch("image/*")
    }
    
    fun openCamera() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    
    fun openTimePicker() {
        val currentHour = if (favoritePairTime.isNotEmpty()) {
            try {
                favoritePairTime.split(":")[0].toInt()
            } catch (e: Exception) {
                calendar.get(Calendar.HOUR_OF_DAY)
            }
        } else {
            calendar.get(Calendar.HOUR_OF_DAY)
        }
        
        val currentMinute = if (favoritePairTime.isNotEmpty()) {
            try {
                favoritePairTime.split(":")[1].toInt()
            } catch (e: Exception) {
                calendar.get(Calendar.MINUTE)
            }
        } else {
            calendar.get(Calendar.MINUTE)
        }
        
        // Используем TimePickerDialog
        android.app.TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val timeString = String.format("%02d:%02d", hourOfDay, minute)
                favoritePairTime = timeString
                timeError = null
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }
    
    fun validateTime(): Boolean {
        return if (favoritePairTime.isBlank()) {
            timeError = null // Пустое поле допустимо
            true
        } else {
            val isValid = NotificationUtils.isValidTimeFormat(favoritePairTime)
            timeError = if (isValid) null else "Некорректный формат времени"
            isValid
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование профиля") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Аватарка с возможностью изменения
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { showImageSourceDialog = true }
            ) {
                if (avatarUri.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(Uri.parse(avatarUri))
                                .build()
                        ),
                        contentDescription = "Аватар",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Аватар",
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Text(
                text = "Нажмите на фото для изменения",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Поля ввода
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("ФИО") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = position,
                        onValueChange = { position = it },
                        label = { Text("Должность") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = resumeUrl,
                        onValueChange = { resumeUrl = it },
                        label = { Text("URL резюме (PDF)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = favoritePairTime,
                        onValueChange = { 
                            favoritePairTime = it
                            timeError = null
                            validateTime()
                        },
                        label = { Text("Время любимой пары (HH:mm)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = { openTimePicker() }) {
                                Icon(
                                    painter = painterResource(android.R.drawable.ic_menu_recent_history),
                                    contentDescription = "Выбрать время"
                                )
                            }
                        },
                        isError = timeError != null,
                        supportingText = timeError?.let { 
                            { Text(it) }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Кнопка сохранения
            Button(
                onClick = {
                    if (validateTime()) {
                        val updatedProfile = UserProfile(
                            fullName = fullName,
                            position = position,
                            resumeUrl = resumeUrl,
                            avatarUri = avatarUri,
                            favoritePairTime = favoritePairTime
                        )
                        
                        viewModel.saveProfile(updatedProfile)
                        
                        // Устанавливаем уведомление, если время указано
                        android.util.Log.e("EditProfileScreen", "=== СОХРАНЕНИЕ ПРОФИЛЯ ===")
                        android.util.Log.e("EditProfileScreen", "Время: $favoritePairTime, Имя: '$fullName'")
                        
                        if (favoritePairTime.isNotEmpty()) {
                            android.util.Log.e("EditProfileScreen", "Устанавливаем уведомление...")
                            
                            // Используем имя пользователя или значение по умолчанию
                            val userName = if (fullName.isNotEmpty()) fullName else "Пользователь"
                            android.util.Log.e("EditProfileScreen", "Имя для уведомления: '$userName'")
                            
                            // Отменяем предыдущее уведомление
                            NotificationUtils.cancelPairNotification(context)
                            
                            // Устанавливаем новое уведомление
                            try {
                                NotificationUtils.schedulePairNotification(
                                    context,
                                    favoritePairTime,
                                    userName
                                )
                                
                                Toast.makeText(
                                    context,
                                    "Профиль сохранен! Уведомление установлено на $favoritePairTime",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                android.util.Log.e("EditProfileScreen", "ОШИБКА при установке уведомления", e)
                                e.printStackTrace()
                                Toast.makeText(
                                    context,
                                    "Ошибка: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            // Если время удалено, отменяем уведомление
                            android.util.Log.e("EditProfileScreen", "Время не указано, отменяем уведомление")
                            NotificationUtils.cancelPairNotification(context)
                        }
                        
                        onBackClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = timeError == null
            ) {
                Text("Готово")
            }
        }
    }
    
    // Диалог выбора источника фото
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Выберите источник") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = {
                            showImageSourceDialog = false
                            openGallery()
                        }
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Галерея")
                    }
                    
                    TextButton(
                        onClick = {
                            showImageSourceDialog = false
                            openCamera()
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Камера")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showImageSourceDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}

