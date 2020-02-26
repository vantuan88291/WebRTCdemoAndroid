package com.tuan88291.webrtcdemo.view.activity.videocall

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tuan88291.webrtcdemo.data.local.model.DataCall
import com.tuan88291.webrtcdemo.data.local.model.DataModel
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

class SocketClient(private val mSocket: Socket): LifecycleObserver {
    val gson = Gson()
    var callbacks: SignallingClientListener? = null
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateSocket() {
        mSocket.run {
            on("Received", onReceived)
            on("onAnswerAccept", onAnswerAccept)
            on("onEndCall", onEndCall)
        }
    }
    fun setCallback(callback: SignallingClientListener) {
        this.callbacks = callback
    }
    fun onCallVideo(data: Any?, model: String) {
        val dataSt = DataCall(model, gson.toJson(data))
        mSocket.emit("call", gson.toJson(dataSt))
    }
    fun onStartCall(model: String) {
        mSocket.emit("startCall", gson.toJson(DataModel(Build.MODEL, model)))
    }
    fun onStartAnswer(model: String) {
        mSocket.emit("startAnswer", gson.toJson(DataModel(model, Build.MODEL)))
    }
    fun endCall(model: String) {
        mSocket.emit("endCall", model)
    }
    private val onAnswerAccept = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            Handler(Looper.getMainLooper()).post {
                callbacks?.onAnswerAccept()
            }
        }
    }
    private val onEndCall = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            Handler(Looper.getMainLooper()).post {
                callbacks?.onEndCall()
            }
        }
    }
    private val onReceived = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            val jsonObject = gson.fromJson(args[0].toString(), JsonObject::class.java)
            Handler(Looper.getMainLooper()).post {
                if (jsonObject.has("serverUrl")) {
                    callbacks?.onIceCandidateReceived(gson.fromJson(jsonObject, IceCandidate::class.java))
                } else if (jsonObject.has("type") && jsonObject.get("type").asString == "OFFER") {
                    callbacks?.onOfferReceived(gson.fromJson(jsonObject, SessionDescription::class.java))
                } else if (jsonObject.has("type") && jsonObject.get("type").asString == "ANSWER") {
                    callbacks?.onAnswerReceived(gson.fromJson(jsonObject, SessionDescription::class.java))
                }
            }
        }
    }
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    fun onClear() {
//        mSocket.emit("clearUser", Build.MODEL)
//    }
}