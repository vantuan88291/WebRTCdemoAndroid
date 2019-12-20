package com.tuan88291.mvvmpattern.view.activity.voicecall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.tuan88291.mvvmpattern.R
import com.tuan88291.mvvmpattern.databinding.ActivityVoiceCallBinding

class VoiceCall : AppCompatActivity() {
    private var binding: ActivityVoiceCallBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voice_call)
//        binding?.timer?.start()
    }
}
