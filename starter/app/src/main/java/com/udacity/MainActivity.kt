package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.ui.ButtonState
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var downloadManager: DownloadManager
    private var downloadID: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        var s="slksldk;lkde;"


        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {

            when (rdG_download_options.checkedRadioButtonId) {
                rdB_download_glide.id -> download(URL_glide, glide_title)
                rdB_download_load_app.id -> download(URL_app_loader, app_loader_title)
                rdB_download_retrofit.id -> download(URL_retrofit, retrofit_title)
                else -> Toast.makeText(this, R.string.please_select_download_option_message, Toast.LENGTH_LONG).show()
            }

        }

        createNotificationChannel(getString(R.string.download_notification_channel_id), getString(R.string.download_notification_channel_name))

    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.e("Download Manager Receiver ", "receiver Completed")

            custom_button.buttonState = ButtonState.Completed

            //DownloadManager.Query() is used to filter DownloadManager queries
            val query = DownloadManager.Query()

            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val downloadTitle = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        context?.let { sendNotification(downloadTitle, getString(R.string.download_completed_label), it) }
                    }
                    DownloadManager.STATUS_FAILED -> {
                        context?.let { sendNotification(downloadTitle, getString(R.string.download_failed_label), it) }
                    }

                }
            }

        }

    }

    fun sendNotification(messageBody: String, downloadStatus: String, context: Context) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(messageBody, downloadStatus, context)
    }

    @SuppressLint("Range")
    private fun download(downloadURL: String, fileName: String) {

        //branch development
         val request =
            DownloadManager.Request(Uri.parse(downloadURL))
                .setTitle(fileName)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        custom_button.buttonState = ButtonState.Loading

    }

    private fun createNotificationChannel(channelID: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(true)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.app_description)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val URL_app_loader = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_glide = "https://codeload.github.com/bumptech/glide/zip/master"
        private const val URL_retrofit = "https://codeload.github.com/square/retrofit/zip/master"

        private const val retrofit_title = "Retrofit"
        private const val app_loader_title = "App Loader"
        private const val glide_title = "Glide"
    }

}
