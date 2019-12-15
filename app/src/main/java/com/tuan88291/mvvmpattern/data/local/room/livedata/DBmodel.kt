package com.tuan88291.mvvmpattern.data.local.room.livedata

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tuan88291.mvvmpattern.data.local.entity.DataRoom
import com.tuan88291.mvvmpattern.data.local.room.QueriesDao

class DBmodel(val mRepository: iDBRepository) : ViewModel() {
    fun getAll(): LiveData<MutableList<DataRoom>> {
        return mRepository.getAll()
    }
    fun getLast(): LiveData<DataRoom> {
        return mRepository.getLast()
    }
    fun insertData(item: DataRoom) {
            mRepository.insertData(item)
    }
}
