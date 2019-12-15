package com.tuan88291.mvvmpattern.view.components

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuan88291.mvvmpattern.data.local.model.DetailUser
import com.tuan88291.mvvmpattern.view.adapter.HomeAdapter

class ListItemUser(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: HomeAdapter? = null

    init {
        setUpList(context)
    }

    private fun setUpList(context: Context) {
        mLayoutManager = LinearLayoutManager(context)
        mAdapter = HomeAdapter(context)
        this.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
    }
    fun setData(data: MutableList<DetailUser>) {
        mAdapter?.setData(data)
    }
}