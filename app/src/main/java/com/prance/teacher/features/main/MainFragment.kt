package com.prance.teacher.features.main

import android.os.Bundle
import android.view.View
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.base.exception.Failure
import com.prance.lib.base.extension.failure
import com.prance.lib.base.extension.observe
import com.prance.lib.base.extension.viewModel
import com.prance.lib.database.UserEntity
import com.prance.lib.sunvote.service.SunVoteService

class MainFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_main

    private lateinit var mMainViewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DaggerMainComponent.create().inject(this)

        mMainViewModel = viewModel(viewModelFactory) {
            observe(movieDetails, ::renderMovieDetails)
            failure(failureData, ::handleFailure)
        }

        activity?.run {
            startService(SunVoteService.callingIntent(this))
        }
    }

    private fun renderMovieDetails(userEntity: UserEntity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.run {
            stopService(SunVoteService.callingIntent(this))
        }
    }


}