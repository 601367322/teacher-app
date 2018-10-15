package com.prance.teacher.features.classes.view

import android.graphics.Rect
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.solver.widgets.ConstraintTableLayout
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Html.fromHtml
import android.text.Spannable
import android.text.SpannableString
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.presenter.ClassesPresenter
import com.prance.lib.common.utils.weight.layoutmanager.PagerGridLayoutManager
import com.prance.lib.common.utils.weight.layoutmanager.PagerGridSnapHelper
import com.prance.lib.spark.SparkService
import com.prance.teacher.BuildConfig
import kotlinx.android.synthetic.main.fragment_classes.*


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

    companion object {
        const val ACTION = "action"
        const val ACTION_TO_CLASS = 0
        const val ACTION_TO_BIND = 1

        fun forAction(action: Int?): ClassesFragment {
            val fragment = ClassesFragment()
            action?.let {
                val arguments = Bundle()
                arguments.putInt(ACTION, action)
                fragment.arguments = arguments
            }
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        layoutManager = PagerGridLayoutManager(
                2, 2, PagerGridLayoutManager.HORIZONTAL)
        recycler.layoutManager = layoutManager

        val pageSnapHelper = PagerGridSnapHelper()
        pageSnapHelper.attachToRecyclerView(recycler)

        layoutManager!!.setPageListener(this)
        layoutManager!!.isAllowContinuousScroll = false

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

//        var span3 =  SpannableString("提示：使用↔键切换班级，点击确认键选择班级")
//        var image =  ImageSpan(this,R.drawable.collect, DynamicDrawableSpan.ALIGN_BOTTOM)
//        span3.setSpan(image,3,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        tv3.setText(span3)
//
//        classesTip.text

        if (BuildConfig.DEBUG) {
//            val list = mutableListOf<ClassesEntity>()
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            list.add(ClassesEntity("呵呵"))
//            mAdapter.setNewData(list)
//            mAdapter.notifyDataSetChanged()
//            return
        }

        refresh.setOnClickListener {
            loadData()
        }

        refresh.performClick()
    }

    private fun loadData() {
        showProgress()

        mPresenter.getAllClasses()

        SparkService.mUsbSerialNum?.run {
            keyPadCount.text = Html.fromHtml("""答题器数 <font color="#3AF0EE">${mPresenter.getKeyPadCount(this)}</font>""")
        }

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


    override fun renderClasses(it: MutableList<ClassesEntity>) {
        hideProgress()
        mAdapter.setNewData(it)
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

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return false
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
}

