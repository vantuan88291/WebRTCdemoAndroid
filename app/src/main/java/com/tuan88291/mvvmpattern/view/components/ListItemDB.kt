package com.tuan88291.mvvmpattern.view.components

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuan88291.mvvmpattern.data.local.entity.DataRoom
import com.tuan88291.mvvmpattern.view.adapter.DBApdapter

class ListItemDB(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: DBApdapter? = null

    init {
        setUpList(context)
    }

    private fun setUpList(context: Context) {
        mLayoutManager = LinearLayoutManager(context)
        mAdapter = DBApdapter(context)
        this.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
    }
    fun setData(data: MutableList<DataRoom>) {
        mAdapter?.setData(data)
    }
}