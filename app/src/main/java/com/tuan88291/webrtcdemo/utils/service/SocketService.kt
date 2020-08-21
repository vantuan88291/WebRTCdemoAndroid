package com.tuan88291.webrtcdemo.utils.service

import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.tuan88291.webrtcdemo.R
import com.tuan88291.webrtcdemo.utils.Common.CHANNEL_ID
import com.tuan88291.webrtcdemo.utils.Common.CHANNEL_ID_FORGROUND
import com.tuan88291.webrtcdemo.utils.Common.CHANNEL_NAME
import com.tuan88291.webrtcdemo.utils.Common.NOTIFY_ID
import com.tuan88291.webrtcdemo.utils.broadcast.HandlerService
import com.tuan88291.webrtcdemo.view.activity.main.MainActivity
import com.tuan88291.webrtcdemo.view.activity.videocall.VideoCall
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.koin.android.ext.android.inject


class SocketService : LifecycleService() {
    private val mSocket: Socket by inject()
    val description = "Webrtc_call_channel"
    var notifyManager: NotificationManager? = null
    companion object checkIsLive {
        var checkrunning: Boolean = false
    }
    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        checkrunning = true
        setupSocket()
        notifyManager = this.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(NOTIFY_ID, setNotification())
        }

    }
    private fun setupSocket() {
        mSocket.run {
            connect()
            on("inComing", onInComing)
            on("onEndCall", onEndCall)
        }
    }
    private val onInComing = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            val NOTIFY_ID_CALL_VIDEO = (System.currentTimeMillis() / 1000).toInt()
            notifyManager?.notify(NOTIFY_ID_CALL_VIDEO, setUpCallHeadup(args[0].toString(), NOTIFY_ID_CALL_VIDEO))
        }
    }
    private val onEndCall = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            notifyManager?.cancelAll()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        checkrunning = false
        mSocket.disconnect()
    }
    private fun setUpCallHeadup(model: String, idNotify: Int): Notification {
        val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.tune)
        val intent: Intent
        val pendingIntent: PendingIntent
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notifyManager?.getNotificationChannel(CHANNEL_ID)
            if (mChannel == null) {
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
                mChannel.description = description
                mChannel.enableVibration(true)
                mChannel.lightColor = Color.GREEN
                mChannel.setSound(soundUri, audioAttributes)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

                notifyManager?.createNotificationChannel(mChannel)
            }
        }

        builder = NotificationCompat.Builder(this, CHANNEL_ID)

        intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        return builder.also {
            it.setContentTitle("One call comming...")
                .setSmallIcon(R.drawable.ic_call)
                .setContentText("$model is calling you...")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setFullScreenIntent(pendingIntent, true)
                .setContentIntent(pendingIntent)
                .setTicker("Notifications")
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))

            val dismissIntent = Intent(this, HandlerService::class.java)
            with(dismissIntent) {
                putExtra("id", idNotify)
                putExtra("model", model)
                putExtra("type", "cancel")
            }
            val pendingDismissIntent = PendingIntent.getBroadcast(this, idNotify,
                dismissIntent, 0);

            val aCceptintent = Intent(this, VideoCall::class.java)
            with(aCceptintent) {
                putExtra("id", idNotify)
                putExtra("model", model)
            }
            val answerIntent = PendingIntent.getActivity(this, idNotify, aCceptintent, 0)

            val dismissAction = NotificationCompat.Action(R.drawable.ic_cancel,
                "Denied", pendingDismissIntent)

            val okAction = NotificationCompat.Action(R.drawable.ic_answer,
                "Answer", answerIntent)
            it.addAction(dismissAction)
            it.addAction(okAction)
        }.build()
    }
    private fun setNotification(): Notification {

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mchanel = NotificationChannel(CHANNEL_ID_FORGROUND, "webrtcdemo simple", NotificationManager.IMPORTANCE_MIN)
            manager.createNotificationChannel(mchanel)
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_FORGROUND)
            .setSmallIcon(R.drawable.ic_noti)
            .setContentTitle("WebRTC is running")
            .setContentText("")
            .setChannelId("webrtcdemo")

        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(contentIntent).build()
        return builder.build()
    }

}