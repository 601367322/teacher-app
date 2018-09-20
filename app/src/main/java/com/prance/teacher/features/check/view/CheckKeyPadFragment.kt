package com.prance.teacher.features.check.view

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.http.ResultException
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.check.contract.ICheckKeyPadContract
import com.prance.teacher.features.check.model.CheckKeyPadGroupTitle
import com.prance.teacher.features.check.presenter.CheckKeyPadPresenter
import com.prance.teacher.features.classes.view.ClassesFragment
import com.spark.teaching.answertool.usb.model.ReceiveAnswer
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.fragment_check_keypad.*
import java.util.concurrent.TimeUnit

/**
 * 答题器检测
 */
class CheckKeyPadFragment : BaseFragment(), ICheckKeyPadContract.View {

    override fun layoutId(): Int = R.layout.fragment_check_keypad

    private var mAction: Int = ACTION_JUST_CHECK
    override var mPresenter: ICheckKeyPadContract.Presenter = CheckKeyPadPresenter()

    private var mMatchKeyPadEntities: MutableList<KeyPadEntity>? = null
    private var mCheckKeyPadEntities: MutableList<KeyPadEntity> = mutableListOf()

    private var mAdapter = CheckKeyPadAdapter(mutableListOf())

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

    private val mSparkServicePresenter by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            var answerList = mutableListOf<Long>()

            override fun onAnswerReceived(answer: ReceiveAnswer) {
                //防止重复提交
                if (answerList.contains(answer.uid)) {
                    return
                }
                answerList.add(answer.uid)

                val keyId = answer.uid.toString()

                if (answer.answer == "H") {
                    mCheckKeyPadEntities.add(KeyPadEntity(keyId, 1F))
                }
            }

            override fun onServiceConnected() {
                super.onServiceConnected()

                check.performClick()
            }

        })
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
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }

        check.setOnClickListener {
            if (SparkService.mUsbSerialNum == null) {
                ToastUtils.showShort("请先链接基站")
                return@setOnClickListener
            }
            showProgress("正在进行检测")

            mCheckKeyPadEntities.clear()

            hideRecycler()

            mPresenter.getMatchedKeyPadByBaseStationId(SparkService.mUsbSerialNum!!)
        }

        mSparkServicePresenter.bind()
    }

    private fun showRecycler() {
        tip.visible()
        recycler.visible()
        linearLayout.visible()

        classTip.invisible()
        classTip1.invisible()
        classTip2.invisible()
    }

    private fun hideRecycler() {
        tip.invisible()
        recycler.invisible()
        linearLayout.invisible()

        classTip.visible()
        classTip1.visible()
        classTip2.visible()
    }

    override fun renderKeyPads(it: MutableList<MultiItemEntity>) {
        hideProgress()

        if (it.isEmpty()) {
            //所有答题器都没问题
            jump.performClick()
        } else {
            showRecycler()

            check.requestFocus()

            mAdapter.setNewData(it)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun startCheck(it: MutableList<KeyPadEntity>) {

        mMatchKeyPadEntities = it

        mSparkServicePresenter.sendQuestion(SparkService.QuestionType.RED_PACKAGE)

        Flowable.timer(2, TimeUnit.SECONDS)
                .mySubscribe {
                    mSparkServicePresenter.stopAnswer()
                    mPresenter.generateGroup(mMatchKeyPadEntities!!, mCheckKeyPadEntities)
                }
    }

    override fun onDestroy() {
        super.onDestroy()

        mSparkServicePresenter.unBind()
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