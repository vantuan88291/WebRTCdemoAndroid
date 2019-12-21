package com.tuan88291.webrtcdemo.data.local.room.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tuan88291.webrtcdemo.data.local.entity.DataRoom

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
