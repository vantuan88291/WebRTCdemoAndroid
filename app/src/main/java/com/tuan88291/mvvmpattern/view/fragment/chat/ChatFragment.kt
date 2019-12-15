package com.tuan88291.mvvmpattern.view.fragment.chat

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.tuan88291.mvvmpattern.BaseFragment
import com.tuan88291.mvvmpattern.R
import com.tuan88291.mvvmpattern.data.local.model.DataChat
import com.tuan88291.mvvmpattern.databinding.AboutFragmentBinding
import com.tuan88291.mvvmpattern.utils.observe.AutoDisposable
import com.tuan88291.mvvmpattern.utils.observe.addTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class ChatFragment : BaseFragment() {
    private var binding: AboutFragmentBinding? = null
    private val chatViewModel: ChatViewModel by viewModel()
    override fun setView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.about_fragment, container, false)
        return binding!!.getRoot()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(chatViewModel)
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.list?.setmId(Build.MODEL)
        chatViewModel.getTyping().observe(this, Observer<String> { this.onTyping(it) })
        chatViewModel.getLoading().observe(this, Observer<Boolean> { this.loading(it) })
        chatViewModel.getDataChat().observe(this, Observer<DataChat> { this.processData(it) })
        chatViewModel.getAllDataChat().observe(this, Observer<MutableList<DataChat>> { this.processAllData(it) })
        binding?.send?.setOnClickListener {
            if (binding?.input?.text?.toString()!! == "") return@setOnClickListener
            chatViewModel.sendMsg(binding?.input?.text?.toString()!!)
            binding?.input?.setText("")
        }
        binding?.input?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                chatViewModel.emitTyping()
            }

        })
        mContext()?.setUpTyping()
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
