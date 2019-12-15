package com.tuan88291.mvvmpattern.utils.observe

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

abstract class ObserveEasy
protected constructor() {
    init {
        onLoading()
        val doWork = Observable.fromCallable { doBackground() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Any>() {

                override fun onNext(o: Any) {
                    try {
                        onSuccess(o)
                    } catch (e: Exception) {
                        onFail(e.message!!)
                    }

                }

                override fun onError(e: Throwable) {
                    onFail(e.message!!)
                    onComplete()
                }

                override fun onComplete() {
                    onLoadComplete()
                }
            })
        if (getDispose() != null)
            doWork.addTo(getDispose()!!)

    }

    protected abstract fun getDispose(): AutoDisposable?
    protected abstract fun doBackground(): Any?

    protected open fun onSuccess(result: Any?) {}

    protected open fun onLoading() {}

    protected open fun onLoadComplete() {}

    protected open fun onFail(err: String) {}
}
