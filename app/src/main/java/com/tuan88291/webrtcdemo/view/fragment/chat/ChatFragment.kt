package com.tuan88291.webrtcdemo.view.fragment.chat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.tuan88291.webrtcdemo.BaseFragment
import com.tuan88291.webrtcdemo.R
import com.tuan88291.webrtcdemo.data.local.model.DataChat
import com.tuan88291.webrtcdemo.databinding.ChatFragmentBinding
import com.tuan88291.webrtcdemo.view.activity.videocall.VideoCall
import com.tuan88291.webrtcdemo.view.activity.voicecall.VoiceCall
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChatFragment : BaseFragment() {
    private var binding: ChatFragmentBinding? = null
    private val chatViewModel: ChatViewModel by viewModel()
    override fun setView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false)
        return binding!!.getRoot()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(chatViewModel)
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.list?.setmId(Build.MODEL)

        chatViewModel.let {chat ->
            chat.getChatState().observe(this, Observer<ChatState> { this.onChangeState(it) })
        }
        binding?.apply {
            send.setOnClickListener {
                if (input.text?.toString()!! == "") return@setOnClickListener
                chatViewModel.sendMsg(input.text?.toString()!!)
                input.setText("")
            }
            input.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    chatViewModel.emitTyping()
                }

            })
            list.clickCall = {item: DataChat, isVideo: Boolean ->
                if (isVideo) {
                    val intent = Intent(mContext(), VideoCall::class.java)
                    intent.putExtra("model", item.name)
                    startActivity(intent)
                } else {
                    val intent = Intent(mContext(), VoiceCall::class.java)
                    intent.putExtra("model", item.name)
                    startActivity(intent)
                }
            }
        }

        mContext()?.setUpTyping()
    }

    private fun onChangeState(state: ChatState) {
        when (state) {
            is ChatState.Loading -> loading(state.loading)
            is ChatState.Success<*> -> processData(state.data as DataChat)
            is ChatState.Typing -> onTyping(state.listTyping)
            is ChatState.AllData<*> -> processAllData(state.data as MutableList<DataChat>)
        }
    }
    private fun processData(item: DataChat) {
        binding?.list?.setData(item)
    }
    private fun processAllData(data: MutableList<DataChat>) {
        binding?.list?.addAllData(data)
    }
    private fun loading(load: Boolean) {
        binding?.loading?.visibility = if (load) View.VISIBLE else View.GONE
    }
    private fun onTyping(typings: String) {
        if (typings !== "") {
            mContext()?.setTyping(typings)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(chatViewModel)
    }
}
