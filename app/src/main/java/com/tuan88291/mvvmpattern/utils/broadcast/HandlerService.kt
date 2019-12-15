package com.tuan88291.mvvmpattern.utils.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.blankj.utilcode.util.ServiceUtils.startService
import com.tuan88291.mvvmpattern.utils.service.SocketService


class HandlerService: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val type = p1?.getExtras()?.getString("type")!!
        val id_notify = p1.getExtras()?.getInt("id")!!
        if (type == "cancel") {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                p0?.startForegroundService(Intent(p0, SocketService::class.java).setAction("STOP_ACTION"))
            } else {
                p0?.startService(Intent(p0, SocketService::class.java).setAction("STOP_ACTION"))
            }
        }

        val closeDialog = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        p0?.sendBroadcast(closeDialog)
    }

}