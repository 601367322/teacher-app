package com.prance.lib.sunvote.service

import android.content.Context
import android.content.Intent
import cn.sunars.sdk.SunARS
import com.prance.lib.base.service.BaseServicePresenter

class SunVoteServicePresenter(context: Context, private var listener: SunARSListenerAdapter) : BaseServicePresenter<SunVoteService.SunVoteServiceBinder>(context) {

    override var mService: SunVoteService.SunVoteServiceBinder? = null

    override var intent: Intent = SunVoteService.callingIntent(context)

    override fun onServiceConnected() {
        this.listener.onServiceConnected()
    }

    override fun bind() {
        SunARS.addListener(listener)
        super.bind()
    }

    override fun unBind() {
        SunARS.removeListener(listener)
        super.unBind()
    }
}