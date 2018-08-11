package com.prance.teacher.features.classes.view

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.presenter.ClassesPresenter
import com.prance.lib.common.utils.weight.layoutmanager.PagerGridLayoutManager
import com.prance.lib.common.utils.weight.layoutmanager.PagerGridSnapHelper
import kotlinx.android.synthetic.main.fragment_classes.*


/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesFragment : BaseFragment(), IClassesContract.View, PagerGridLayoutManager.PageListener {


    override var mPresenter: IClassesContract.Presenter = ClassesPresenter()

    var mAdapter: ClassesAdapter = ClassesAdapter()

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
                } else {
                    outRect?.left = resources.getDimensionPixelOffset(R.dimen.m20_0)
                }
            }
        })

        recycler.adapter = mAdapter

        refresh.setOnClickListener {
            loadData()
        }

        refresh.performClick()
    }

    private fun loadData() {
        showProgress()

        mPresenter.getAllClasses()
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

            if (pageIndex < pageIndicatorView.count - 1) {
                rightArrow.visibility = View.VISIBLE
            } else {
                rightArrow.visibility = View.GONE
            }
            if (pageIndex > 0) {
                leftArrow.visibility = View.VISIBLE
            } else {
                rightArrow.visibility = View.GONE
            }
        }
    }


    override fun renderClasses(it: MutableList<ClassesEntity>) {
        hideProgress()
        mAdapter.data = it
        mAdapter.notifyDataSetChanged()

        if (layoutManager?.totalPageCount!! > 0) {
            rightArrow.visibility = View.VISIBLE

            pageIndicatorView.count = layoutManager?.totalPageCount!!
        }

        checkEmpty()
    }

    override fun layoutId(): Int = R.layout.fragment_classes

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return false
    }

    override fun needSunVoteService(): Boolean = true

    private fun checkEmpty() {
        if (mAdapter.data.isEmpty()) {
            emptyLayout.visibility = View.VISIBLE
            recycler.visibility = View.GONE

            refresh.requestFocus()
        } else {
            emptyLayout.visibility = View.GONE
            recycler.visibility = View.VISIBLE

            recycler.requestFocus()
        }
    }
}

