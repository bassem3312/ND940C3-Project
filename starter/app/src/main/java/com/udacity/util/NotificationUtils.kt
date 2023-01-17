package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R


const val EXTRA_DOWNLOAD_FILE_NAME = "FILE_NAME"
const val EXTRA_DOWNLOAD_STATUS = "STATUS"
const val NOTIFICATION_ID: Int = 12220

/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/12/2023.
 */


fun NotificationManager.sendNotification(messageBody: String, status: String, appContext: Context) {

    val contentIntent = Intent(appContext, DetailActivity::class.java)
    contentIntent.putExtra(EXTRA_DOWNLOAD_FILE_NAME, messageBody)
    contentIntent.putExtra(EXTRA_DOWNLOAD_STATUS, status)
    val pendingIntent: PendingIntent = PendingIntent.getActivity(appContext, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    //Bassem Mohsen

    val downloadImage = BitmapFactory.decodeResource(
        appContext.resources,
        R.drawable.ic_download
    )

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(downloadImage)
        .bigLargeIcon(null)

    val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    var builder = NotificationCompat.Builder(appContext, appContext.getString(R.string.download_notification_channel_id))
        .setSmallIcon(R.drawable.ic_downloading_notification)
        .setContentTitle(appContext.getString(R.string.app_description))
        .setContentText("$messageBody $status")
        .setOnlyAlertOnce(true)
        .setSound(alarmSound)
        .setStyle(bigPicStyle)
        .setLargeIcon(downloadImage)
        .addAction(
            android.R.drawable.ic_menu_info_details, appContext.getString(R.string.download_detail),
            pendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())

}
