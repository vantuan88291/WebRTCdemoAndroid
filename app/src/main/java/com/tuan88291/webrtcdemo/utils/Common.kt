package com.tuan88291.webrtcdemo.utils

import android.Manifest

object Common {
    val DOMAIN = "http://192.168.0.0:3000"
    val SHARED_PREFERENCE_NAME = "AppName"
    val NOTIFY_ID = 1273485
    val CHANNEL_NAME = "WebrtcApp"
    val CHANNEL_ID = "Webrtc_app"
    val CHANNEL_ID_FORGROUND = "webrtcdemo"
    val CAMERA_PERMISSION_REQUEST_CODE = 101
    val AUDIO_PERMISSION_REQUEST_CODE = 104
    val CAMERA_PERMISSION = Manifest.permission.CAMERA
    val AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
}
