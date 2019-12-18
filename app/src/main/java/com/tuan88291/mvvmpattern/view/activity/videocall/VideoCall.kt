package com.tuan88291.mvvmpattern.view.activity.videocall

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.tuan88291.mvvmpattern.BaseActivity
import com.tuan88291.mvvmpattern.R
import com.tuan88291.mvvmpattern.databinding.ActivityVideoCallBinding
import com.tuan88291.mvvmpattern.utils.Common.AUDIO_PERMISSION
import com.tuan88291.mvvmpattern.utils.Common.CAMERA_PERMISSION
import com.tuan88291.mvvmpattern.utils.Common.CAMERA_PERMISSION_REQUEST_CODE
import com.tuan88291.mvvmpattern.utils.webrtc.AppSdpObserver
import com.tuan88291.mvvmpattern.utils.webrtc.PeerConnectionObserver
import com.tuan88291.mvvmpattern.utils.webrtc.RTCClient
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription
import org.koin.android.ext.android.inject

class VideoCall : BaseActivity(), SignallingClientListener {
    private var binding: ActivityVideoCallBinding? = null
    private var rtcClient: RTCClient? = null
    private val videoModel: SocketClient by inject()
    private var model: String = ""
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_call)
        videoModel.setCallback(this)
        checkCameraPermission()
        binding?.endCall?.setOnClickListener {
            finish()
        }
    }

    private fun checkIsCalling() {
        val id = getIntent().getIntExtra("id", 0)
        val model = getIntent().getStringExtra("model")
        if (id != 0) {
            this.model = if (model != null) model else ""
            videoModel.onStartAnswer(this.model)
            clearNotification(id)
            return
        }
        if (model != null) {
            this.model = model
            Toast.makeText(this, "Calling to $model ...", Toast.LENGTH_SHORT).show()
            videoModel.onStartCall(model)
        }
    }

    override fun onAnswerAccept() {
        rtcClient?.call(sdpObserver)
    }
    override fun onOfferReceived(data: SessionDescription) {
        rtcClient?.onRemoteSessionReceived(data)
        rtcClient?.answer(sdpObserver)
        binding?.constraintLayout5?.transitionToEnd()
    }
    override fun onAnswerReceived(data: SessionDescription) {
        rtcClient?.onRemoteSessionReceived(data)
        binding?.constraintLayout5?.transitionToEnd()
    }
    override fun onIceCandidateReceived(data: IceCandidate) {
        rtcClient?.addIceCandidate(data)
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, AUDIO_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            onCameraPermissionGranted()
        }
    }

    private fun onCameraPermissionGranted() {
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
                    p0?.videoTracks?.get(0)?.addSink(binding?.remoteView)
                }
            }
        )
        rtcClient?.initSurfaceView(binding?.remoteView!!)
        rtcClient?.initSurfaceView(binding?.localView!!)
        rtcClient?.startLocalVideoCapture(binding?.localView!!)
        checkIsCalling()

    }
    private fun requestCameraPermission(dialogShown: Boolean = false) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA_PERMISSION) && !dialogShown) {
            showPermissionRationaleDialog()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA_PERMISSION, AUDIO_PERMISSION), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Camera Permission Required")
            .setMessage("This app need the camera to function")
            .setPositiveButton("Grant") { dialog, _ ->
                dialog.dismiss()
                requestCameraPermission(true)
            }
            .setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                onCameraPermissionDenied()
            }
            .show()
    }
    private fun onCameraPermissionDenied() {
        Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            onCameraPermissionGranted()
        } else {
            onCameraPermissionDenied()
        }
    }
    private fun clearNotification(id: Int) {
        val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifyManager.cancel(id)
    }

    override fun onStop() {
        super.onStop()
        rtcClient?.close()
    }
}
