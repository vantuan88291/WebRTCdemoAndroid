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
import com.tuan88291.mvvmpattern.utils.Common.CHANNEL_ID
import com.tuan88291.mvvmpattern.utils.Common.CHANNEL_ID_FORGROUND
import com.tuan88291.mvvmpattern.utils.Common.CHANNEL_NAME
import com.tuan88291.mvvmpattern.utils.Common.NOTIFY_ID
import com.tuan88291.mvvmpattern.utils.Common.NOTIFY_ID_CALL_VIDEO
import com.tuan88291.mvvmpattern.utils.broadcast.HandlerService
import com.tuan88291.mvvmpattern.view.activity.MainActivity
import com.tuan88291.mvvmpattern.view.activity.videocall.VideoCall
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
    override fun onBind(intent: Intent?): IBinder? {
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
        mSocket.connect()
        mSocket.on("inComing", onInComing)
    }
    private val onInComing = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            notifyManager?.notify(NOTIFY_ID_CALL_VIDEO, setUpCallHeadup())
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        checkrunning = false
        mSocket.emit("clearUser", Build.MODEL)
        mSocket.disconnect()
    }
    private fun setUpCallHeadup(): Notification {
        val intent: Intent
        val pendingIntent: PendingIntent
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notifyManager?.getNotificationChannel(CHANNEL_ID)
            if (mChannel == null) {
                mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
                mChannel.description = description
                mChannel.enableVibration(true)
                mChannel.lightColor = Color.GREEN
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notifyManager?.createNotificationChannel(mChannel)
            }
        }

        builder = NotificationCompat.Builder(this, CHANNEL_ID)

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
        dismissIntent.putExtra("id", NOTIFY_ID_CALL_VIDEO)
        dismissIntent.putExtra("type", "cancel")
        val pendingDismissIntent = PendingIntent.getBroadcast(this, NOTIFY_ID_CALL_VIDEO,
            dismissIntent, 0);

        val aCceptintent = Intent(this, VideoCall::class.java)
        aCceptintent.putExtra("id", NOTIFY_ID_CALL_VIDEO)
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
        builder.setContentIntent(contentIntent)
        return builder.build()
    }

}