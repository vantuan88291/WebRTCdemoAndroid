package com.tuan88291.mvvmpattern.data.local.room.livedata

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.tuan88291.mvvmpattern.data.local.entity.DataRoom
import com.tuan88291.mvvmpattern.data.local.room.QueriesDao
import com.tuan88291.mvvmpattern.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
@SuppressLint("CheckResult")
class DBRepository internal constructor(val mQueries: QueriesDao): iDBRepository {
    override fun getAll(): LiveData<MutableList<DataRoom>> {
        return mQueries.getAll()
    }

    override fun getLast(): LiveData<DataRoom> {
        return mQueries.getLast()
    }

    override fun insertData(item: DataRoom) {
        mQueries.insertData(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Utils.log("Insert db success", item) },
                { Utils.log("Insert db Fail", item) }
            )
    }
}
