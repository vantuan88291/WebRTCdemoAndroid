package com.tuan88291.mvvmpattern

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import com.tuan88291.mvvmpattern.di.RetrofitModule
import com.tuan88291.mvvmpattern.di.dbModule
import com.tuan88291.mvvmpattern.di.mvvmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : MultiDexApplication() {
    init {
        instance = this
        gSon = Gson()
    }
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            androidFileProperties()
            modules(listOf(dbModule, mvvmModule, RetrofitModule))
        }
    }

    companion object {
        private var instance: App? = null
        private var gSon: Gson? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
        fun getGson() : Gson {
            return gSon!!
        }
    }
}

