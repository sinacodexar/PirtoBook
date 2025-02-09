package ir.sina.pirtobook.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import ir.sina.pirtobook.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadService(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationId = 1

    fun downloadAndOpenPDF(url: String) {
        val fileName = url.substringAfterLast("/") + ".pdf" // نام فایل بر اساس لینک
        val file = File(context.getExternalFilesDir(null), fileName)

        if (file.exists()) {
            openPDF(file) // اگر فایل وجود دارد، مستقیماً باز شود
            return
        }

        showNotification(0) // شروع دانلود با نوتیفیکیشن پیشرفت

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.connect()

                val fileLength = urlConnection.contentLength // حجم فایل برای نمایش درصد پیشرفت
                val inputStream = urlConnection.inputStream
                val outputStream = FileOutputStream(file)

                val buffer = ByteArray(4096)
                var total: Long = 0
                var count: Int

                while (inputStream.read(buffer).also { count = it } != -1) {
                    total += count
                    outputStream.write(buffer, 0, count)
                    val progress = ((total * 100) / fileLength).toInt()
                    showNotification(progress) // به‌روزرسانی نوتیفیکیشن
                }

                outputStream.close()
                inputStream.close()

                withContext(Dispatchers.Main) {
                    showCompletedNotification(file) // نمایش نوتیفیکیشن تکمیل دانلود
                    openPDF(file) // باز کردن فایل بعد از دانلود
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showErrorNotification() // نمایش خطا در صورت ناموفق بودن دانلود
                }
            }
        }
    }

    // نمایش نوتیفیکیشن پیشرفت
    private fun showNotification(progress: Int) {
        val builder = NotificationCompat.Builder(context, "download_channel")
            .setSmallIcon( R.drawable.ic_download_noti )
            .setContentTitle("در حال دانلود فایل...")
            .setContentText("$progress% تکمیل شده")
            .setProgress(100, progress, false)
            .setOngoing(true)

        notificationManager.notify(notificationId, builder.build())
    }

    // نمایش نوتیفیکیشن تکمیل دانلود
    private fun showCompletedNotification(file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val openIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, "download_channel")
            .setSmallIcon(R.drawable.check_noti)
            .setContentTitle("دانلود کامل شد")
            .setContentText("برای باز کردن فایل کلیک کنید")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }

    // نمایش نوتیفیکیشن خطا
    private fun showErrorNotification() {
        val builder = NotificationCompat.Builder(context, "download_channel")
            .setSmallIcon(R.drawable.nodownload_noti)
            .setContentTitle("خطا در دانلود")
            .setContentText("دانلود فایل با مشکل مواجه شد.")
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }

    // باز کردن فایل PDF
    private fun openPDF(file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "هیچ برنامه‌ای برای باز کردن PDF یافت نشد!", Toast.LENGTH_SHORT).show()
        }
    }
}
