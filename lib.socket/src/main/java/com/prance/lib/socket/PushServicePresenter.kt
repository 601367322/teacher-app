package com.prance.lib.socket

import android.content.Context
import android.content.Intent
import com.prance.lib.base.service.BaseServicePresenter

class PushServicePresenter(context: Context, private var messageListener: MessageListener) : BaseServicePresenter<PushService.PushServiceBinder>(context) {

    override var mService: PushService.PushServiceBinder? = null

    override var intent: Intent = PushService.callingIntent(context)

    override fun onServiceConnected() {
        mService?.addListener(this.messageListener)
    }

    override fun unBind() {
        mService?.removeListener(messageListener)
        super.unBind()
    }
}