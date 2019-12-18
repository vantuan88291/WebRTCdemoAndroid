package com.tuan88291.mvvmpattern.utils.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.socket.client.Socket
import org.koin.core.KoinComponent
import org.koin.core.inject


class HandlerService: BroadcastReceiver(), KoinComponent {
    private val mSocket: Socket by inject()
    override fun onReceive(p0: Context?, p1: Intent?) {
        val type = p1?.getExtras()?.getString("type")!!
        val model = p1.getExtras()?.getString("model")!!
        val id_notify = p1.getExtras()?.getInt("id")!!
        if (type == "cancel") {
            val notifyManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifyManager.cancel(id_notify)
            mSocket.emit("endCall", model)
        }

        val closeDialog = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        p0?.sendBroadcast(closeDialog)
    }

}