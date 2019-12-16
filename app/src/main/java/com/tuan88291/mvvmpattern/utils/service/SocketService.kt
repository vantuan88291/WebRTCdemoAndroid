package com.tuan88291.mvvmpattern.utils.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.tuan88291.mvvmpattern.R
import com.tuan88291.mvvmpattern.utils.broadcast.HandlerService
import com.tuan88291.mvvmpattern.view.activity.MainActivity
import com.tuan88291.mvvmpattern.view.activity.videocall.VideoCall
import io.socket.client.Socket
import org.koin.android.ext.android.inject


class SocketService : LifecycleService() {
    private val mSocket: Socket by inject()
    val notification_id: Int = 1273485
    val NOTIFY_ID = 1002
    val name = "WebrtcApp"
    val id = "Webrtc_app"
    val description = "Webrtc_call_channel"
    var notifyManager: NotificationManager? = null
    companion object checkIsLive {
        var checkrunning: Boolean = false
    }
    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if(intent?.getAction() != null && intent.getAction() == "STOP_ACTION") {
            stopForeground(false)
            notifyManager?.cancel(NOTIFY_ID)
        }
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        checkrunning = true
        mSocket.connect()
        if (notifyManager == null) {
            notifyManager = this.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(notification_id, setNotification())
        }
        val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notifyManager.notify(NOTIFY_ID, setUpCallHeadup())
    }

    override fun onDestroy() {
        super.onDestroy()
        checkrunning = false
        mSocket.disconnect()
    }
    private fun setUpCallHeadup(): Notification {
        val intent: Intent
        val pendingIntent: PendingIntent
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notifyManager?.getNotificationChannel(id)
            if (mChannel == null) {
                mChannel = NotificationChannel(id, name, importance)
                mChannel.description = description
                mChannel.enableVibration(true)
                mChannel.lightColor = Color.GREEN
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notifyManager?.createNotificationChannel(mChannel)
            }
        }

        builder = NotificationCompat.Builder(this, id)

        intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        builder.setContentTitle("One call comming...")  // required
            .setSmallIcon(R.drawable.ic_call) // required
            .setContentText("Some one call you")  // required
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setTicker("Notifications")
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))

        val dismissIntent = Intent(this, HandlerService::class.java)
        dismissIntent.putExtra("id", NOTIFY_ID)
        dismissIntent.putExtra("type", "cancel")
        val pendingDismissIntent = PendingIntent.getBroadcast(this, NOTIFY_ID,
            dismissIntent, 0);

        val aCceptintent = Intent(this, VideoCall::class.java)
        aCceptintent.putExtra("id", NOTIFY_ID)
        val answerIntent = PendingIntent.getActivity(this, 0, aCceptintent, 0)

        val dismissAction = NotificationCompat.Action(R.drawable.ic_cancel,
            "Denied", pendingDismissIntent)

        val okAction = NotificationCompat.Action(R.drawable.ic_answer,
            "Answer", answerIntent)
        builder.addAction(dismissAction)
        builder.addAction(okAction)

        return builder.build()
    }
    private fun setNotification(): Notification {

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mchanel = NotificationChannel("webrtcdemo", "webrtcdemo simple", NotificationManager.IMPORTANCE_MIN)
            manager.createNotificationChannel(mchanel)
        }
        val builder = NotificationCompat.Builder(this, "webrtcdemo")
            .setSmallIcon(R.drawable.ic_noti)
            .setContentTitle("WebRTC is running")
            .setContentText("")
            .setChannelId("webrtcdemo")

        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(contentIntent)
        return builder.build()
    }

}