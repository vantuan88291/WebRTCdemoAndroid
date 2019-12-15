package com.tuan88291.mvvmpattern.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tuan88291.mvvmpattern.BR
import com.tuan88291.mvvmpattern.data.local.entity.DataRoom
import com.tuan88291.mvvmpattern.databinding.ItemDbBinding

class DBApdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater
    internal var data = mutableListOf<DataRoom>()
    private var binding: ItemDbBinding? = null

    init {
        inflater = LayoutInflater.from(context)
    }
    fun setData(data: MutableList<DataRoom>) {
        this.data = data
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemDbBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data.getOrElse(position) { data.getOrNull(0) }
        val mHolder = holder as MyHolder
        mHolder.bind(item!!)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    internal inner class MyHolder(binding: ItemDbBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemDbBinding? = null
        init {
            this.binding = binding
        }
        fun bind(item: Any) {
            this.binding?.setVariable(BR.userDB, item)
        }
    }
}
