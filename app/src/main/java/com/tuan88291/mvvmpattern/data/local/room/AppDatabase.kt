package com.tuan88291.mvvmpattern.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tuan88291.mvvmpattern.data.local.entity.DataRoom

@Database(entities = [DataRoom::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun queries() : QueriesDao
}