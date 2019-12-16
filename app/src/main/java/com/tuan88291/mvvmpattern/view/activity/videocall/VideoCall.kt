package com.tuan88291.mvvmpattern.view.activity.videocall

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.tuan88291.mvvmpattern.BaseActivity
import com.tuan88291.mvvmpattern.R
import com.tuan88291.mvvmpattern.databinding.ActivityVideoCallBinding
import com.tuan88291.mvvmpattern.utils.service.SocketService

class VideoCall : BaseActivity() {
    private var binding: ActivityVideoCallBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_call)
        val id = getIntent().getIntExtra("id", 0)
        clearNotification()
        binding?.endCall?.setOnClickListener {
//            binding?.constraintLayout5?.transitionToEnd()
            finish()
        }

    }
    private fun clearNotification() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            this.startForegroundService(Intent(this, SocketService::class.java).setAction("STOP_ACTION"))
        } else {
            this.startService(Intent(this, SocketService::class.java).setAction("STOP_ACTION"))
        }
    }
}
