package com.prance.lib.common.utils

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.SizeUtils

class ImageConfig {

        internal var mActivity: Activity? = null
        internal var mFragment: Fragment? = null
        internal var mContext: Context? = null
        internal var mFragmentActivity: FragmentActivity? = null
        internal var mView: View? = null

        internal var mUrl: String? = null
        internal var mImageView: ImageView? = null

        internal var mBitmapTypes: MutableList<Int> = ArrayList()

        /**
         * 圆角大小
         */
        internal var mBorderRadius: Int = 0

        constructor(activity: Activity) {
            this.mActivity = activity
        }

        constructor(fragmentActivity: FragmentActivity) {
            this.mFragmentActivity = fragmentActivity
        }

        constructor(view: View) {
            this.mView = view
        }

        constructor(context: Context) {
            this.mContext = context
        }

        constructor(fragment: Fragment) {
            this.mFragment = fragment
        }

        fun setUrl(url: String): ImageConfig {
            this.mUrl = url
            return this
        }

        fun setImageView(imageView: ImageView): ImageConfig {
            this.mImageView = imageView
            return this
        }

        fun setBitmapTyes(vararg types: Int): ImageConfig {
            for (type in types) {
                mBitmapTypes.add(type)
            }
            return this
        }

        fun setBorderRadius(borderRadius: Int): ImageConfig {
            this.mBorderRadius = SizeUtils.dp2px(borderRadius.toFloat())
            return this
        }

        companion object {

            val CENTER_CROP = 1
            val AS_BITMAP = 2
            val CIRCLE = 3
        }

    }