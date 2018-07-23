package com.prance.lib.teacher.base.weight

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.*
import com.prance.lib.common.utils.weight.layoutmanager.PagerGridLayoutManager

class PageTvRecyclerView : RecyclerView {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        this.initView()
    }

    private fun initView() {
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
        setHasFixedSize(true)
        setWillNotDraw(true)
        overScrollMode = View.OVER_SCROLL_NEVER
        isChildrenDrawingOrderEnabled = true

        clipChildren = false
        clipToPadding = false

        isClickable = false
        isFocusable = true
        isFocusableInTouchMode = true
        /**
         * 防止RecyclerView刷新时焦点不错乱bug的步骤如下:
         * (1)adapter执行setHasStableIds(true)方法
         * (2)重写getItemId()方法,让每个view都有各自的id
         * (3)RecyclerView的动画必须去掉
         */
        itemAnimator = null
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val result = super.dispatchKeyEvent(event)

        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
            return result
        }

        val focusView = this.focusedChild ?: return result

        val position = layoutManager.getPosition(focusView)

        if (event.action == KeyEvent.ACTION_UP) run { return true }

        when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (isRightEdge(focusView)) {
                    layoutManager.findViewByPosition(position + 1)?.requestFocus()
                    if (isPageLastEdge(focusView)) {
                        (layoutManager as PagerGridLayoutManager).smoothNextPage()
                    }
                } else {
                    return false
                }
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (isLeftEdge(focusView)) {
                    layoutManager.findViewByPosition(position - 1)?.requestFocus()
                    if (isPageFirstEdge(focusView)) {
                        (layoutManager as PagerGridLayoutManager).smoothPrePage()
                    }
                } else {
                    return false
                }
                return true
            }
        }

        return result
    }

    private fun isRightEdge(view: View): Boolean {
        val layoutManager = layoutManager as PagerGridLayoutManager
        val columnNum: Int = layoutManager.columnNum

        val position = layoutManager.getPosition(view) + 1
        if (position % columnNum == 0) {
            return true
        }
        return false
    }

    private fun isLeftEdge(view: View): Boolean {
        val layoutManager = layoutManager as PagerGridLayoutManager
        val columnNum: Int = layoutManager.columnNum

        val position = layoutManager.getPosition(view) + 1
        if (position % columnNum == 1) {
            return true
        }
        return false
    }

    private fun isPageFirstEdge(view: View): Boolean {
        val layoutManager = layoutManager as PagerGridLayoutManager
        val pageItemCount = layoutManager.columnNum * layoutManager.rouNum

        val position = layoutManager.getPosition(view) + 1
        if (position % pageItemCount == 1) {
            return true
        }
        return false
    }

    private fun isPageLastEdge(view: View): Boolean {
        val layoutManager = layoutManager as PagerGridLayoutManager
        val pageItemCount = layoutManager.columnNum * layoutManager.rouNum

        val position = layoutManager.getPosition(view) + 1
        if (position % pageItemCount == 0) {
            return true
        }
        return false
    }
}