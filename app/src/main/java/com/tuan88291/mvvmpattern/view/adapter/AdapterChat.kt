package com.tuan88291.mvvmpattern.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.tuan88291.mvvmpattern.BR
import com.tuan88291.mvvmpattern.R
import com.tuan88291.mvvmpattern.data.local.model.DataChat
import com.tuan88291.mvvmpattern.databinding.ItemChatFriendBinding
import com.tuan88291.mvvmpattern.databinding.ItemChatYouBinding

class AdapterChat(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater
    internal var data = mutableListOf<DataChat>()
    private var name: String? = null
    var clickCall: ((DataChat, Boolean)->Unit)? = null
    init {
        inflater = LayoutInflater.from(context)
    }
    fun setId(name: String) {
     this.name = name
    }
    fun setData(item: DataChat) {
        this.data.add(item)
        notifyItemInserted(this.data.size - 1)
    }
    fun addAllData(data: MutableList<DataChat>) {
        this.data = data
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (this.data[viewType].name == name) {
            return MyHolder(ItemChatYouBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        return FriendHolder(ItemChatFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data.getOrElse(position) { data.getOrNull(0) }
        if (holder is MyHolder) {
            holder.bind(item!!)
        } else {
            val mHolder = holder as FriendHolder
            mHolder.bind(item!!)
            val popupMenu = PopupMenu(context, mHolder.binding?.call!!)
            popupMenu.inflate(R.menu.menu_popup)
            mHolder.binding?.call?.setOnClickListener {
                popupMenu.show()
            }
            popupMenu.setOnMenuItemClickListener(
                object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(items: MenuItem): Boolean {
                        when (items.getItemId()) {
                            R.id.callVideo -> clickCall?.invoke(item, true)
                            R.id.callVoice -> clickCall?.invoke(item, false)
                        }
                        return true
                    }
                })

        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    internal inner class MyHolder(binding: ItemChatYouBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemChatYouBinding? = null
        init {
            this.binding = binding
        }
        fun bind(item: Any) {
            this.binding?.setVariable(BR.userChat, item)
        }
    }
    internal inner class FriendHolder(binding: ItemChatFriendBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemChatFriendBinding? = null
        init {
            this.binding = binding
        }
        fun bind(item: Any) {
            this.binding?.setVariable(BR.userChat, item)
        }
    }
}