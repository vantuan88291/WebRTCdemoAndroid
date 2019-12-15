package com.tuan88291.mvvmpattern.view.fragment.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.tuan88291.mvvmpattern.BaseFragment
import com.tuan88291.mvvmpattern.R
import com.tuan88291.mvvmpattern.data.local.entity.DataRoom
import com.tuan88291.mvvmpattern.data.local.model.DataUser
import com.tuan88291.mvvmpattern.data.local.model.DetailUser
import com.tuan88291.mvvmpattern.data.local.room.livedata.DBmodel
import com.tuan88291.mvvmpattern.databinding.HomeFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
    private val homeViewModel: HomeViewModel by viewModel()
    private var binding: HomeFragmentBinding? = null
    private val db: DBmodel by viewModel()

    override fun setView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding!!.getRoot()
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {

        homeViewModel.getData().observe(this, Observer<DataUser> { this.processData(it) })
        homeViewModel.loading().observe(this, Observer<Boolean> { this.loading(it) })
        homeViewModel.error().observe(this, Observer<String> { this.error(it) })

        db.getAll().observe(this, Observer<MutableList<DataRoom>> { this.onDataChange(it) })

        binding?.button?.setOnClickListener{
            homeViewModel.loadData()
        }
        binding?.btn?.setOnClickListener {
            db.insertData(DataRoom("tuan", (0..10).random()))
        }
    }
    private fun onDataChange(data: MutableList<DataRoom>) {
        binding?.list?.visibility = View.GONE
        binding?.listDb?.visibility = View.VISIBLE
        binding?.listDb?.setData(data)
    }
    private fun processData(data: DataUser) {
        binding?.list?.visibility = View.VISIBLE
        binding?.listDb?.visibility = View.GONE
        try {
            binding?.list?.setData(data.data!!)
        }catch (e: Exception) {
        }
    }
    private fun loading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun error(msg: String) {
    }
    fun toast(msg: String) {
        Toast.makeText(mContext(), msg, Toast.LENGTH_LONG).show()
    }
}
