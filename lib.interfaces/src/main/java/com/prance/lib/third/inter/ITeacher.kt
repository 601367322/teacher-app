package com.prance.lib.third.inter

import android.content.Context
import android.content.Intent

interface ITeacher {

    fun getMainIntent(context: Context): Intent
}