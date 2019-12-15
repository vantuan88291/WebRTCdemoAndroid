package com.tuan88291.mvvmpattern.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class DataRoom( @ColumnInfo(name = "name") var Name: String = "", @ColumnInfo(name = "age") var Age: Int = 0) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var mId: Long = 0
}