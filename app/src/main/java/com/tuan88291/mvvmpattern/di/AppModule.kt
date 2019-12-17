package com.tuan88291.mvvmpattern.di

import android.os.Build
import androidx.room.Room
import com.tuan88291.mvvmpattern.data.local.room.AppDatabase
import com.tuan88291.mvvmpattern.data.local.room.livedata.DBRepository
import com.tuan88291.mvvmpattern.data.local.room.livedata.DBmodel
import com.tuan88291.mvvmpattern.data.local.room.livedata.iDBRepository
import com.tuan88291.mvvmpattern.data.remote.ApiGenerator
import com.tuan88291.mvvmpattern.data.remote.service.ServiceApi
import com.tuan88291.mvvmpattern.data.remote.service.iServiceApi
import com.tuan88291.mvvmpattern.view.activity.videocall.SocketClient
import com.tuan88291.mvvmpattern.view.fragment.chat.ChatViewModel
import io.socket.client.IO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "user-database").build() }
    single { get<AppDatabase>().queries() }
    single<iDBRepository> { DBRepository(get()) }
    viewModel { DBmodel(get()) }
}
val mvvmModule = module {
    viewModel { ChatViewModel(get()) }
}
val RetrofitModule = module {
    single<iServiceApi> { ServiceApi() }
    single { ApiGenerator(get()) }
}
val socketModule = module {
    single {
        val opts = IO.Options()
        opts.query = "model=" + Build.MODEL
        IO.socket("http://192.168.31.196:3000", opts)
    }
    factory { SocketClient(get()) }
}