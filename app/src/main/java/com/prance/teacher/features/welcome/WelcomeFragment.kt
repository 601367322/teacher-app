package com.prance.teacher.features.welcome

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.login.view.UpdateFragment
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class WelcomeFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_welcome

    var mDisposable: Disposable? = null

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mDisposable = Flowable.timer(3, TimeUnit.SECONDS)
                .mySubscribe {
                    (activity as FragmentActivity).supportFragmentManager.inTransaction {
                        replace(R.id.fragmentContainer, UpdateFragment())
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()

        mDisposable?.dispose()
        mDisposable = null
    }
}