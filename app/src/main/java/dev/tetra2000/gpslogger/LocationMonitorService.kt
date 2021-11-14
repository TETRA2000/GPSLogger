package dev.tetra2000.gpslogger

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi

class LocationMonitorService : Service() {
    private val ONGOING_NOTIFICATION_ID = 1

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId =
            createNotificationChannel("my_service", "My Background Service")

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification =
            Notification.Builder(this, channelId)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
//            .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.ticker_text))
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // https://developer.android.com/about/versions/11/privacy/foreground-services
            startForeground(ONGOING_NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(ONGOING_NOTIFICATION_ID, notification)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    // https://stackoverflow.com/a/47533338
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}