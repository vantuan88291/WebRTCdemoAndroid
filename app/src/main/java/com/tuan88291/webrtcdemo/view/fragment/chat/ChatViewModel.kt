package com.tuan88291.webrtcdemo.view.fragment.chat

import android.os.Build
import androidx.lifecycle.*
import com.tuan88291.webrtcdemo.data.local.model.DataChat
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject


class ChatViewModel(private val mSocket: Socket): ViewModel(), LifecycleObserver {
    private val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    private val listTyping: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val data: MutableLiveData<DataChat> by lazy { MutableLiveData<DataChat>() }
    private val allData: MutableLiveData<MutableList<DataChat>> by lazy { MutableLiveData<MutableList<DataChat>>() }
    val mId: Int = (0..10).random()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateSocket() {
        mSocket.on("newmsg", onNewMsg)
        mSocket.on("allData", getAllData)
        mSocket.on("isTyping", onTyping)
        mSocket.emit("getAllData")

    }
    fun sendMsg(msg: String) {
        val jsonObject = JSONObject()
        jsonObject.put("id", mId)
        jsonObject.put("name", Build.MODEL)
        jsonObject.put("message", msg)
        mSocket.emit("sendmsg", jsonObject)
    }
    private val onNewMsg = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            val datas = args[0] as JSONObject
            val item = DataChat(datas.getInt("id"), datas.getString("name"), datas.getString("message"))
            data.postValue(item)
        }
    }
    private val getAllData = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            val datas = args[0] as JSONArray
            if (datas.length() > 0) {
                val data =  mutableListOf<DataChat>()
                for (i in 0..datas.length() - 1) {
                    val jsObject = datas[i] as JSONObject
                    data.add(DataChat(jsObject.getInt("id"), jsObject.getString("name"), jsObject.getString("message")))
                }
                allData.postValue(data)
            }
            isLoading.postValue(false)
        }
    }
    private val onTyping = object : Emitter.Listener {

        override fun call(vararg args: Any?) {
            val datas = args[0].toString()
            var listUser = ""
            if (datas != Build.MODEL) {
                listUser = "$datas"
            }
            listTyping.postValue(listUser)
        }
    }
    fun emitTyping() {
        mSocket.emit("typing", Build.MODEL)
    }
    fun getDataChat(): MutableLiveData<DataChat>{
        return this.data
    }
    fun getLoading(): MutableLiveData<Boolean>{
        return this.isLoading
    }
    fun getTyping(): MutableLiveData<String>{
        return this.listTyping
    }
    fun getAllDataChat(): MutableLiveData<MutableList<DataChat>>{
        return this.allData
    }
}