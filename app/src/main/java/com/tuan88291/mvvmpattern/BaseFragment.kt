package com.tuan88291.mvvmpattern

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tuan88291.mvvmpattern.view.activity.MainActivity

abstract class BaseFragment : Fragment(), BaseView {
    private var context: MainActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setErrorParent(data: Any) {
        AlertDialog.Builder(context!!)
            .setTitle("Your Alert")
            .setMessage(data.toString())
            .setCancelable(false)
            .setPositiveButton("ok") { dialog, which ->

            }.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return setView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context as MainActivity?
    }

    protected abstract fun setView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    protected abstract fun viewCreated(view: View, savedInstanceState: Bundle?)
    protected fun mContext(): MainActivity? {
        return this.context
    }

}
