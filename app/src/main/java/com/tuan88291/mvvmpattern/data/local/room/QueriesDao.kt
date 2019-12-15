package com.tuan88291.mvvmpattern.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tuan88291.mvvmpattern.data.local.entity.DataRoom
import io.reactivex.Completable

@Dao
interface QueriesDao {

    @Query("select * from user ORDER BY id DESC")
    fun getAll(): LiveData<MutableList<DataRoom>>

    @Insert
    fun insertData(item: DataRoom) : Completable

    @Query("SELECT * FROM user where id = (SELECT MAX(id) FROM user) LIMIT 1")
    fun getLast(): LiveData<DataRoom>
}