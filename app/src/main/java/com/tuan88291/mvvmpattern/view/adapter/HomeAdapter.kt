package com.tuan88291.mvvmpattern.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tuan88291.mvvmpattern.BR
import com.tuan88291.mvvmpattern.data.local.model.DetailUser
import com.tuan88291.mvvmpattern.databinding.ItemHomeBinding


class HomeAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater
    internal var data = mutableListOf<DetailUser>()
    private var binding: ItemHomeBinding? = null

    init {
        inflater = LayoutInflater.from(context)
    }
    fun setData(data: MutableList<DetailUser>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    internal inner class MyHolder(binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemHomeBinding? = null
        init {
            this.binding = binding
        }
        fun bind(item: Any) {
            this.binding?.setVariable(BR.user, item)
        }
    }
}
