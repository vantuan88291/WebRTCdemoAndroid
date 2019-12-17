package com.tuan88291.mvvmpattern.view.activity.videocall

import android.os.Build
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tuan88291.mvvmpattern.data.local.model.DataCall
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

class VideoViewModel(private val mSocket: Socket): ViewModel(), LifecycleObserver {
    val onOfferReceived: MutableLiveData<SessionDescription> by lazy { MutableLiveData<SessionDescription>() }
    val onAnswerReceived: MutableLiveData<SessionDescription> by lazy { MutableLiveData<SessionDescription>() }
    val onIceCandidateReceived: MutableLiveData<IceCandidate> by lazy { MutableLiveData<IceCandidate>() }
    val gson = Gson()
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateSocket() {
        mSocket.on("Received", onReceived)
        mSocket.emit("getAllData")

    }
    fun onCallVideo(data: Any?) {
        val dataSt = DataCall(Build.MODEL, gson.toJson(data))
        mSocket.emit("call", gson.toJson(dataSt))
    }
    private val onReceived = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            val jsonObject = gson.fromJson(args[0].toString(), JsonObject::class.java)
            if (jsonObject.has("serverUrl")) {
                onIceCandidateReceived.postValue(gson.fromJson(jsonObject, IceCandidate::class.java))
            } else if (jsonObject.has("type") && jsonObject.get("type").asString == "OFFER") {
                onOfferReceived.postValue(gson.fromJson(jsonObject, SessionDescription::class.java))
            } else if (jsonObject.has("type") && jsonObject.get("type").asString == "ANSWER") {
                onAnswerReceived.postValue(gson.fromJson(jsonObject, SessionDescription::class.java))
            }
        }
    }
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    fun onClear() {
//        mSocket.emit("clearUser", Build.MODEL)
//    }
}