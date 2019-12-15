package com.tuan88291.mvvmpattern.utils.observe

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class AutoDisposable(lifecycle: Lifecycle?) : LifecycleObserver {
    var compositeDisposable: CompositeDisposable? = null
    init {
        lifecycle?.addObserver(this)
        compositeDisposable = CompositeDisposable()
    }

    fun add(disposable: Disposable) {
        if (compositeDisposable != null) {
            compositeDisposable?.add(disposable)
        } else {
            throw NotImplementedError("must bind AutoDisposable to a Lifecycle first")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        compositeDisposable?.dispose()
        LogUtils.a("Destroy this task success----->")
    }
    fun onDismiss() {
        compositeDisposable?.dispose()
        LogUtils.a("Cancel this task success----->")
    }
}

fun Disposable.addTo(autoDisposable: AutoDisposable) {
    autoDisposable.add(this)
}