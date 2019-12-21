package com.tuan88291.webrtcdemo.view.components

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuan88291.webrtcdemo.data.local.model.DataChat
import com.tuan88291.webrtcdemo.view.adapter.AdapterChat

class ListChatSocket(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: AdapterChat? = null
    var clickCall: ((DataChat, Boolean)->Unit)? = null
    init {
        setUpList(context)
    }

    private fun setUpList(context: Context) {
        mLayoutManager = LinearLayoutManager(context)
        mAdapter = AdapterChat(context)
        this.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
        mAdapter?.clickCall = {item: DataChat, isVideo: Boolean ->
            clickCall?.invoke(item, isVideo)
        }
    }
    fun setmId(name: String) {
        mAdapter?.setId(name)
    }
    fun setData(item: DataChat) {
        mAdapter?.setData(item)
        this.smoothScrollToPosition(mAdapter!!.itemCount - 1)
    }
    fun addAllData(data: MutableList<DataChat>) {
        mAdapter?.addAllData(data)
        this.scrollToEnd()
    }
    fun scrollToEnd() {
        try {
            this.smoothScrollToPosition(mAdapter!!.itemCount - 1)
        }catch (e: Exception) {

        }
    }
}