package com.tuan88291.mvvmpattern.data.remote.customcallback

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.tuan88291.mvvmpattern.utils.observe.AutoDisposable
import com.tuan88291.mvvmpattern.utils.observe.addTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseRetrofit<T>(callback: Observable<T>) {

    init {
        getRetrofit(callback)
    }

    private fun getRetrofit(callback: Observable<T>) {
        onLoading()
        if (callback != null) {
            val dis = callback.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<T>() {
                    override fun onComplete() {
                        onLoadComplete()
                    }
                    override fun onNext(t: T) {
                        try {
                            onGetApiComplete(t)
                        } catch (e: Exception) {
                            if (e.localizedMessage == null) {
                                onFail("Have some trouble, please try again")
                            } else {
                                onFail(e.localizedMessage)
                            }
                            LogUtils.a("Exeption fire: $e")
                        }
                        onLoadComplete()
                    }

                    override fun onError(e: Throwable) {
                        try {
                            if (e is HttpException) {
                                val responseBody = e.response().errorBody()
                                onFail(getMessage(responseBody!!))
                            } else if (e is SocketTimeoutException) {
                                onFail("Server not response, please try again")
                            } else if (e is IOException) {
                                onFail("Check your network please!")
                            } else {
                                if (e.localizedMessage == null) {
                                    onFail("Have some trouble, please try again")
                                } else {
                                    onFail(e.localizedMessage)
                                }

                            }

                        }catch (e1: Exception){
                            onFail(e1.localizedMessage)

                        }
                        LogUtils.a("onError: $e")
                        onLoadComplete()
                    }

                })
            if (getDispose() != null) {
                dis.addTo(getDispose()!!)
            }

        }else{
            onFail("callback can not be null")
            LogUtils.a("callback null")
            onLoadComplete()
        }
    }


    private fun getMessage(response: ResponseBody): String {
        var mess: String
        try {
            val errorBody = response.string()
            val jObjError = JSONObject(errorBody)
            mess = jObjError.getString("message")
        } catch (e: Exception) {
            mess = "Have some trouble, please try again!"
            LogUtils.a("Can not get message!", response.toString())
        }
        return mess
    }
    protected abstract fun getDispose(): AutoDisposable?
    protected abstract fun onGetApiComplete(t: T)

    protected open fun onLoading() {

    }

    protected open fun onLoadComplete() {

    }

    protected abstract fun onFail(err: String)

}
