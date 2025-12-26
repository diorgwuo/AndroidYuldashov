package com.example.practike3andr.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

object FileUtils {
    
    sealed class DownloadResult {
        object Success : DownloadResult()
        data class Error(val message: String) : DownloadResult()
    }
    
    suspend fun downloadAndOpenPdf(context: Context, url: String): DownloadResult {
        return withContext(Dispatchers.IO) {
            try {
                // Проверка валидности URL
                if (url.isBlank()) {
                    return@withContext DownloadResult.Error("URL резюме не указан")
                }
                
                val urlObj = try {
                    URL(url)
                } catch (e: Exception) {
                    return@withContext DownloadResult.Error("Некорректный URL: ${e.message}")
                }
                
                // Проверка протокола
                val protocol = urlObj.protocol.lowercase()
                if (protocol != "http" && protocol != "https") {
                    return@withContext DownloadResult.Error("Поддерживаются только HTTP и HTTPS протоколы")
                }
                
                // Используем DownloadManager для скачивания в публичную папку Downloads
                withContext(Dispatchers.Main) {
                    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    
                    val fileName = "resume_${System.currentTimeMillis()}.pdf"
                    
                    val request = DownloadManager.Request(Uri.parse(url)).apply {
                        setTitle("Резюме")
                        setDescription("Скачивание резюме")
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                        setMimeType("application/pdf")
                        allowScanningByMediaScanner()
                    }
                    
                    val downloadId = downloadManager.enqueue(request)
                    
                    // Ждем завершения загрузки и открываем файл
                    withContext(Dispatchers.IO) {
                        var finished = false
                        var attempts = 0
                        val maxAttempts = 60 // Максимум 60 секунд ожидания
                        
                        while (!finished && attempts < maxAttempts) {
                            delay(1000) // Ждем 1 секунду
                            val query = DownloadManager.Query().setFilterById(downloadId)
                            val cursor = downloadManager.query(query)
                            
                            if (cursor.moveToFirst()) {
                                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                                
                                when (status) {
                                    DownloadManager.STATUS_SUCCESSFUL -> {
                                        finished = true
                                        val uriString = cursor.getString(
                                            cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI)
                                        )
                                        
                                        withContext(Dispatchers.Main) {
                                            if (uriString != null) {
                                                val fileUri = Uri.parse(uriString)
                                                openPdfFile(context, fileUri, fileName)
                                            }
                                        }
                                    }
                                    DownloadManager.STATUS_FAILED -> {
                                        finished = true
                                        val reason = cursor.getInt(
                                            cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON)
                                        )
                                        val errorMessage = when (reason) {
                                            DownloadManager.ERROR_CANNOT_RESUME -> "Не удалось возобновить загрузку"
                                            DownloadManager.ERROR_DEVICE_NOT_FOUND -> "Устройство хранения не найдено"
                                            DownloadManager.ERROR_FILE_ALREADY_EXISTS -> "Файл уже существует"
                                            DownloadManager.ERROR_FILE_ERROR -> "Ошибка файловой системы"
                                            DownloadManager.ERROR_HTTP_DATA_ERROR -> "Ошибка HTTP данных"
                                            DownloadManager.ERROR_INSUFFICIENT_SPACE -> "Недостаточно места на устройстве"
                                            DownloadManager.ERROR_TOO_MANY_REDIRECTS -> "Слишком много перенаправлений"
                                            DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> "Необработанный HTTP код"
                                            else -> "Ошибка загрузки: код $reason"
                                        }
                                        return@withContext DownloadResult.Error(errorMessage)
                                    }
                                }
                            }
                            cursor.close()
                            attempts++
                        }
                        
                        if (!finished) {
                            return@withContext DownloadResult.Error("Превышено время ожидания загрузки")
                        }
                    }
                }
                
                DownloadResult.Success
            } catch (e: java.net.SocketTimeoutException) {
                DownloadResult.Error("Превышено время ожидания. Проверьте интернет-соединение")
            } catch (e: java.net.UnknownHostException) {
                DownloadResult.Error("Не удалось подключиться к серверу. Проверьте интернет-соединение")
            } catch (e: java.io.IOException) {
                DownloadResult.Error("Ошибка при скачивании файла: ${e.message ?: "Неизвестная ошибка"}")
            } catch (e: Exception) {
                DownloadResult.Error("Ошибка: ${e.message ?: "Неизвестная ошибка"}")
            }
        }
    }
    
    private fun openPdfFile(context: Context, uri: Uri, fileName: String): Boolean {
        return try {
            // Пытаемся открыть PDF файл через просмотрщик
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            // Если есть приложение для просмотра PDF, открываем файл
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                true
            } else {
                // Если нет приложения для PDF, пробуем открыть файл как обычный файл
                // Android предложит выбрать приложение
                val genericIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "*/*")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                
                if (genericIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(genericIntent)
                    true
                } else {
                    // Если ничего не работает, открываем папку Downloads
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsDir, fileName)
                    
                    if (file.exists()) {
                        val fileUri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
                            )
                        } else {
                            Uri.fromFile(file)
                        }
                        
                        val fileIntent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(fileUri, "*/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        
                        if (fileIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(fileIntent)
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

