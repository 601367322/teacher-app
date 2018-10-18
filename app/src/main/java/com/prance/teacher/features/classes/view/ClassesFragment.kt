package com.prance.teacher.features.classes.view

import android.graphics.ColorSpace
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.teacher.R
import com.prance.teacher.features.classes.presenter.ClassesPresenter
import com.prance.lib.common.utils.weight.layoutmanager.PagerGridLayoutManager
import com.prance.lib.common.utils.weight.layoutmanager.PagerGridSnapHelper
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.teacher.BuildConfig
import com.prance.teacher.R.id.*
import com.prance.teacher.features.classes.ClassesNextStepActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.common.NetErrorFragment
import com.prance.teacher.features.login.view.UpdateFragment
import com.prance.teacher.features.main.MainActivity
import com.spark.teaching.answertool.usb.model.ReportBindCard
import kotlinx.android.synthetic.main.fragment_classes.*
import org.greenrobot.eventbus.EventBus


/**
 * Description : 班级列表界面
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesFragment : BaseFragment(), IClassesContract.View, PagerGridLayoutManager.PageListener {


    override var mPresenter: IClassesContract.Presenter = ClassesPresenter()

    var mAdapter: ClassesAdapter = ClassesAdapter(R.layout.item_classes)

    var layoutManager: PagerGridLayoutManager? = null

    private val mSparkServicePresenter: SparkServicePresenter  by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {
            override fun onServiceConnected() {
                super.onServiceConnected()

                updateKeyPadCountTip()
            }
        })
    }

    private var mLastFocusPosition: Int? = 0

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        layoutManager = PagerGridLayoutManager(
                2, 2, PagerGridLayoutManager.HORIZONTAL)
        recycler.layoutManager = layoutManager

        val pageSnapHelper = PagerGridSnapHelper()
        pageSnapHelper.attachToRecyclerView(recycler)

        layoutManager!!.setPageListener(this)
        layoutManager!!.isAllowContinuousScroll = false

        mAdapter.setOnItemClickListener { _, _, position ->
            val classVo = mAdapter.getItem(position)

            if (!BuildConfig.DEBUG && SparkService.mUsbSerialNum == null) {
                ToastUtils.showShort("请连接接收器")
                return@setOnItemClickListener
            }

            classVo?.run {
                EventBus.getDefault().post(MainActivity.EventMainClassesEntity(ClassesEntity(id!!, name!!)))
                activity?.run {
                    startActivity(ClassesNextStepActivity.callingIntent(this, classVo))
                }
            }
        }

        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                val position = layoutManager!!.getPosition(view!!)
                if ((position + 1) % 2 == 1) {
                    outRect?.left = resources.getDimensionPixelOffset(R.dimen.m120_0)
                    outRect?.right = resources.getDimensionPixelOffset(R.dimen.m20_0)
                } else {
                    outRect?.left = resources.getDimensionPixelOffset(R.dimen.m20_0)
                    outRect?.right = resources.getDimensionPixelOffset(R.dimen.m120_0)
                }

                outRect?.bottom = resources.getDimensionPixelOffset(R.dimen.m40_0)
            }
        })

        recycler.setPadding(0, (ScreenUtils.getScreenHeight() - resources.getDimensionPixelOffset(R.dimen.m860_0)) / 2, 0, (ScreenUtils.getScreenHeight() - resources.getDimensionPixelOffset(R.dimen.m860_0)) / 2)

        recycler.adapter = mAdapter

        refresh.setOnClickListener {
            loadData()
        }

        refresh.performClick()

        mSparkServicePresenter.bind()
    }

    private fun loadData() {
        showProgress()

        mPresenter.getAllClasses(true)
    }

    override fun onPageSizeChanged(pageSize: Int) {
        pageIndicatorView.post {
            pageIndicatorView.count = pageSize
            pageIndicatorView.setSelected(0)
        }
    }

    override fun onPageSelect(pageIndex: Int) {
        pageIndicatorView.post {
            pageIndicatorView.setSelected(pageIndex)

            if (pageIndex == 0) {
                leftArrow.invisible()
            } else {
                leftArrow.visible()
            }


            if (pageIndex == layoutManager?.totalPageCount!! - 1 || mAdapter.data.isEmpty()) {
                rightArrow.invisible()
            } else {
                rightArrow.visible()
            }


        }
    }


    override fun renderClasses(it: MutableList<ClassVo>) {
        hideProgress()
        mAdapter.setNewData(it)
//        mAdapter.addData(it)
//        mAdapter.addData(it)
//        mAdapter.addData(it)
//        mAdapter.addData(it)
        mAdapter.notifyDataSetChanged()

        rightArrow.post {
            if (layoutManager?.totalPageCount!! > 1) {
                rightArrow.visible()
                pageIndicatorView.count = layoutManager?.totalPageCount!!
            }
        }

        checkEmpty()
    }

    override fun layoutId(): Int = R.layout.fragment_classes

    override fun onResume() {
        super.onResume()

        updateKeyPadCountTip()

        if (isPaused) {
            mLastFocusPosition = recycler.layoutManager?.getPosition(recycler.layoutManager?.focusedChild)
            mPresenter.getAllClasses(false)
        }
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    private var isPaused = false

    override fun refreshClasses(list: MutableList<ClassVo>) {
        for (newC in list) {
            for (oldC in mAdapter.data) {
                if (newC.id == oldC.id) {
                    oldC.studentCount += newC.studentCount + 1
                    oldC.bindingCount += newC.bindingCount + 1
                }
            }
        }
        mAdapter.notifyItemRangeInserted(0,mAdapter.itemCount)
    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        context?.let {
            (activity as FragmentActivity).supportFragmentManager.inTransaction {
                replace(R.id.fragmentContainer, NetErrorFragment.callIntent {
                    (it as FragmentActivity).supportFragmentManager.inTransaction {
                        replace(R.id.fragmentContainer, ClassesFragment())
                    }
                })
            }
        }
        return true
    }

    private fun updateKeyPadCountTip() {
        SparkService.mUsbSerialNum?.run {
            keyPadCount.text = Html.fromHtml("""答题器数 <font color="#3AF0EE">${mPresenter.getKeyPadCount(this)}</font>""")
        }
    }

    private fun checkEmpty() {
        if (mAdapter.data.isEmpty()) {
            emptyLayout.visible()
            recycler.invisible()

            refresh.requestFocus()
        } else {
            emptyLayout.invisible()
            recycler.visible()

            recycler.requestFocus()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mSparkServicePresenter.unBind()
    }
}

