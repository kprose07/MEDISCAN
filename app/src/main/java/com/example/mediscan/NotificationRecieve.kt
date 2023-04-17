package com.example.mediscan

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mediscan.Fragments.SavedFragment

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageEtra = "messageExtra"

class NotificationRecieve : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification: Notification = NotificationCompat.Builder(context,channelID)
            .setSmallIcon(R.drawable.logom)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageEtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID,notification)

    }
}