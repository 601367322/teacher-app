package com.prance.teacher.features.main

import android.arch.lifecycle.MutableLiveData
import com.prance.lib.base.platform.BaseViewModel
import com.prance.lib.database.UserEntity
import javax.inject.Inject

class MainViewModel
@Inject constructor() : BaseViewModel() {

    var movieDetails: MutableLiveData<UserEntity> = MutableLiveData()


}