package com.tuan88291.webrtcdemo.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tuan88291.webrtcdemo.data.local.entity.DataRoom

@Database(entities = [DataRoom::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun queries() : QueriesDao
}