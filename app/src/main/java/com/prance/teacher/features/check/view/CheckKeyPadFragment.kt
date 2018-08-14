package com.prance.teacher.features.check.view

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.http.ResultException
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.check.contract.ICheckKeyPadContract
import com.prance.teacher.features.check.model.CheckKeyPadGroupTitle
import com.prance.teacher.features.check.presenter.CheckKeyPadPresenter
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.utils.IntentUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_check_keypad.*
import java.util.concurrent.TimeUnit

class CheckKeyPadFragment : BaseFragment(), ICheckKeyPadContract.View {

    override fun layoutId(): Int = R.layout.fragment_check_keypad

    private var mAction: Int = ACTION_JUST_CHECK
    override var mPresenter: ICheckKeyPadContract.Presenter = CheckKeyPadPresenter()

    private var mMatchKeyPadEntities: MutableList<KeyPadEntity>? = null
    private var mCheckKeyPadEntities: MutableList<KeyPadEntity> = mutableListOf()

    private var mAdapter = CheckKeyPadAdapter()

    companion object {

        const val ACTION_JUST_CHECK = 3

        fun forAction(action: Int?): CheckKeyPadFragment {
            val fragment = CheckKeyPadFragment()
            action?.let {
                val arguments = Bundle()
                arguments.putInt(ClassesFragment.ACTION, action)
                fragment.arguments = arguments
            }
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        arguments?.run {
            mAction = getInt(ClassesFragment.ACTION, ACTION_JUST_CHECK)
        }

        recycler.adapter = mAdapter
        val layoutManager = GridLayoutManager(context, 7)

        //设置行距
        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                val position = layoutManager.getPosition(view!!)
                //如果不是分组标题
                if (mAdapter.data[position] is KeyPadEntity) {
                    outRect?.bottom = resources.getDimensionPixelOffset(R.dimen.m48_0)
                }
            }
        })

        //设置分组标题占据列宽
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position < mAdapter.data.size) {
                    if (mAdapter.data[position] is CheckKeyPadGroupTitle) {
                        return 7
                    }
                }
                return 1
            }
        }
        recycler.layoutManager = layoutManager

        //开始连接视频
        jump.setOnClickListener {
            context?.run {
                try {
                    startActivity(IntentUtils.callingXYDial())
                } catch (e: Exception) {
                    e.printStackTrace()
                    ToastUtils.showShort("请使用小鱼易联")
                }
            }
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }

        check.setOnClickListener {
            if (application.mBaseStation.sn == null) {
                ToastUtils.showShort("请先链接基站")
                return@setOnClickListener
            }
            showProgress("正在进行检测")

            hideRecycler()

            mPresenter.getMatchedKeyPadByBaseStationId(application.mBaseStation.sn)
        }

        check.performClick()
    }

    fun showRecycler() {
        tip.visibility = View.VISIBLE
        recycler.visibility = View.VISIBLE
        linearLayout.visibility = View.VISIBLE

        classTip.visibility = View.GONE
        classTip1.visibility = View.GONE
        classTip2.visibility = View.GONE
    }

    fun hideRecycler() {
        tip.visibility = View.GONE
        recycler.visibility = View.GONE
        linearLayout.visibility = View.GONE

        classTip.visibility = View.VISIBLE
        classTip1.visibility = View.VISIBLE
        classTip2.visibility = View.VISIBLE
    }

    override fun renderKeyPads(it: MutableList<Any>) {
        hideProgress()

        if (it.isEmpty()) {
            //所有答题器都没问题
            jump.performClick()
        } else {
            showRecycler()

            check.requestFocus()

            mAdapter.data = it
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun startCheck(it: MutableList<KeyPadEntity>) {

        mMatchKeyPadEntities = it

        SunARS.checkKeyPad()

        Flowable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe({
                    SunARS.voteStop()

                    mPresenter.generateGroup(mMatchKeyPadEntities!!, mCheckKeyPadEntities)
                })
    }

    override fun needSunVoteService(): Boolean = true

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        //4 2 0.0 0,0,2.92,34
        val keyId = generateKeyPadId(KeyID)
        when (iMode) {
        //键盘检测结果
            SunARS.KeyResult_status -> {
                sInfo?.let {
                    val status = it.split(",")
                    var battery = status[2].toFloat()
                    mCheckKeyPadEntities.add(KeyPadEntity(keyId, battery))
                }
            }
        }

    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        if (throwable is ResultException) {
            when (throwable.status) {
                88002 -> {
                    activity?.finish()
                    return false
                }
            }
        }
        showRecycler()
        return false
    }
}