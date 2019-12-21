package com.tuan88291.webrtcdemo.view.activity.voicecall

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.tuan88291.webrtcdemo.R
import com.tuan88291.webrtcdemo.databinding.ActivityVoiceCallBinding
import com.tuan88291.webrtcdemo.utils.Common
import com.tuan88291.webrtcdemo.utils.webrtc.AppSdpObserver
import com.tuan88291.webrtcdemo.utils.webrtc.PeerConnectionObserver
import com.tuan88291.webrtcdemo.utils.webrtc.RTCClient
import com.tuan88291.webrtcdemo.view.activity.videocall.SignallingClientListener
import com.tuan88291.webrtcdemo.view.activity.videocall.SocketClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription
import java.util.concurrent.TimeUnit

class VoiceCall : AppCompatActivity(), SignallingClientListener {
    private var binding: ActivityVoiceCallBinding? = null
    private var rtcClient: RTCClient? = null
    private val videoModel: SocketClient by inject()
    private var model: String = ""
    private var task: Disposable? = null
    private var mAudio: AudioManager? = null
    private var isSpeaker: Boolean = false
    private val sdpObserver = object : AppSdpObserver() {
        override fun onCreateSuccess(p0: SessionDescription?) {
            super.onCreateSuccess(p0)
            videoModel.onCallVideo(p0, model)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(videoModel)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voice_call)
        mAudio = getApplicationContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        videoModel.setCallback(this)
        checkVoicePermission()
        binding?.changeMic?.setOnClickListener {
            if (isSpeaker) {
                setHeadsetOff()
            } else {
                setHeadsetOn()
            }
        }
    }
    private fun checkVoicePermission() {
        if ( ContextCompat.checkSelfPermission(this,
                Common.AUDIO_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            setUpRtcClient()
        }
    }
    private fun requestCameraPermission(dialogShown: Boolean = false) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Common.AUDIO_PERMISSION) && !dialogShown) {
            showPermissionRationaleDialog()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(
                Common.AUDIO_PERMISSION
            ), Common.AUDIO_PERMISSION_REQUEST_CODE
            )
        }
    }
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Audio Permission Required")
            .setMessage("This app need the Audio, micro to function")
            .setPositiveButton("Grant") { dialog, _ ->
                dialog.dismiss()
                requestCameraPermission(true)
            }
            .setNegativeButton("Deny") { dialog, _ ->
                Toast.makeText(this, "Audio Not accept", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Common.AUDIO_PERMISSION_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            setUpRtcClient()
        } else {
            Toast.makeText(this, "Audio Not accept", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setHeadsetOn() {
        isSpeaker = true
        binding?.changeMic?.setImageResource(R.drawable.ic_speaker)
        mAudio?.setSpeakerphoneOn(true)
        mAudio?.setMode(AudioManager.MODE_IN_COMMUNICATION)
    }
    private fun setHeadsetOff() {
        isSpeaker = false
        binding?.changeMic?.setImageResource(R.drawable.ic_mute)
        mAudio?.setSpeakerphoneOn(false)
        mAudio?.setMode(AudioManager.MODE_IN_COMMUNICATION)
    }

    override fun onEndCall() {
        binding?.apply {
            name.text = "$model was end the call"
            timer.visibility = View.VISIBLE
            timer.stop()
        }
        finish()
    }

    private fun checkIsCalling() {
        val id = getIntent().getIntExtra("id", 0)
        val model = getIntent().getStringExtra("model")
        if (id != 0) {
            this.model = if (model != null) model else ""
            binding?.name?.text = "Calling to $model"
            videoModel.onStartAnswer(this.model)
            clearNotification(id)
            return
        }
        if (model != null) {
            this.model = model
            task = Observable.timer(40000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Toast.makeText(this, "Not answer!", Toast.LENGTH_SHORT).show()
                    videoModel.endCall(this.model)
                    rtcClient?.close()
                    Handler().postDelayed({
                        finish()
                    }, 2000)
                }
        }
    }
    private fun setUpRtcClient() {
        rtcClient = RTCClient(
            application,
            object : PeerConnectionObserver() {
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    videoModel.onCallVideo(p0, model)
                    rtcClient?.addIceCandidate(p0)
                }

                override fun onAddStream(p0: MediaStream?) {
                    super.onAddStream(p0)
                    p0?.audioTracks?.get(0)?.setEnabled(true)
                }
            }
        )
//        checkIsCalling()

    }
    override fun onAnswerAccept() {
        task?.dispose()
    }
    override fun onOfferReceived(data: SessionDescription) {
        rtcClient?.onRemoteSessionReceived(data)
        binding?.apply {
            timer.visibility = View.VISIBLE
            timer.start()
        }
    }
    override fun onAnswerReceived(data: SessionDescription) {
        rtcClient?.onRemoteSessionReceived(data)
        binding?.apply {
            timer.visibility = View.VISIBLE
            timer.start()
        }
    }
    override fun onIceCandidateReceived(data: IceCandidate) {
        rtcClient?.addIceCandidate(data)
    }
    private fun clearNotification(id: Int) {
        val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifyManager.cancel(id)
    }
    override fun onStop() {
        super.onStop()
        binding?.timer?.stop()
        task?.dispose()
        videoModel.endCall(this.model)
        rtcClient?.close()
    }
}
