package com.tuan88291.mvvmpattern.data.local.room.livedata

import androidx.lifecycle.LiveData
import com.tuan88291.mvvmpattern.data.local.entity.DataRoom

interface iDBRepository {
    fun getAll(): LiveData<MutableList<DataRoom>>
    fun getLast(): LiveData<DataRoom>
    fun insertData(item: DataRoom)
}