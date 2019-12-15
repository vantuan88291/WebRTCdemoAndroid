package com.tuan88291.mvvmpattern.view.fragment.homefragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuan88291.mvvmpattern.data.local.model.DataUser
import com.tuan88291.mvvmpattern.data.remote.ApiGenerator
import com.tuan88291.mvvmpattern.data.remote.BaseInteractor
import com.tuan88291.mvvmpattern.data.remote.CallApi
import com.tuan88291.mvvmpattern.data.remote.customcallback.BaseRetrofit
import com.tuan88291.mvvmpattern.utils.observe.AutoDisposable

class HomeViewModel(api: ApiGenerator): ViewModel(), BaseInteractor {
    override val callAPi: CallApi = api.createApi()

    private val dataServer: MutableLiveData<DataUser> by lazy { MutableLiveData<DataUser>() }
    private val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    private val error: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val autodis = AutoDisposable(null)
    fun getData(): MutableLiveData<DataUser>{
        return this.dataServer
    }
    fun loading(): MutableLiveData<Boolean>{
        return this.isLoading
    }
    fun error(): MutableLiveData<String>{
        return this.error
    }
    fun loadData(){
        object : BaseRetrofit<DataUser>(callAPi.getList(1)) {
            override fun onFail(err: String) {
                error.postValue(err)
            }

            override fun onLoading() {
                super.onLoading()
                isLoading.postValue(true)
            }

            override fun onLoadComplete() {
                super.onLoadComplete()
                isLoading.postValue(false)
            }

            override fun getDispose(): AutoDisposable? {
                return autodis
            }

            override fun onGetApiComplete(t: DataUser) {
                dataServer.postValue(t)
            }
        }
    }
    fun dispose() {
        autodis.onDismiss()
    }
}