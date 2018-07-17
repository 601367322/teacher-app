package com.prance.lib.teacher.base.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.prance.lib.database.BaseStationEntity

class BaseStationViewModel : ViewModel() {

    var mBaseStation: MutableLiveData<BaseStationEntity>? = null

    public fun getBaseStation(): LiveData<BaseStationEntity> {
        if (mBaseStation == null)
            mBaseStation = MutableLiveData()
        return mBaseStation!!
    }
}