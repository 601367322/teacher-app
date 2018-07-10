package com.prance.lib.common.utils

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.Utils

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.annotation.GlideModule

import java.util.ArrayList
import com.bumptech.glide.module.AppGlideModule
import com.prance.lib.common.utils.GlideOptions.bitmapTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient


/**
 * 图片加载类
 * 提供图片加载接口，方便以后替换第三方图片加载库
 * Created by shenbingbing on 16/4/27.
 *
 * ImageLoaderFactory.displayImage(
 *    ImageConfig(this)
 *       .setUrl("http://img3.imgtn.bdimg.com/it/u=1327933654,3965063595&fm=27&gp=0.jpg")
 *       .setBorderRadius(20)
 *       .setImageView(image)
)
 */
@GlideModule
class ImageLoaderFactory : AppGlideModule() {

    companion object {

        fun init(mOkHttpClient: OkHttpClient) {
            this.mOkHttpClient = mOkHttpClient
        }

        var mOkHttpClient: OkHttpClient? = null

        fun displayImage(config: ImageConfig) {
            var mGlideRequests: GlideRequests? = null

            mGlideRequests = mGlideRequests ?: config.mActivity?.let {
                GlideApp.with(it)
            }
            mGlideRequests = mGlideRequests ?: config.mView?.let {
                GlideApp.with(it)
            }
            mGlideRequests = mGlideRequests ?: config.mFragment?.let {
                GlideApp.with(it)
            }
            mGlideRequests = mGlideRequests ?: config.mContext?.let {
                GlideApp.with(it)
            }
            mGlideRequests = mGlideRequests ?: config.mFragmentActivity?.let {
                GlideApp.with(it)
            }

            val requestBuilder = mGlideRequests?.load(config.mUrl)

            config.mBitmapTypes.forEach({ i ->
                run {
                    when (i) {
                        ImageConfig.AS_BITMAP -> mGlideRequests?.asBitmap()
                        ImageConfig.CENTER_CROP -> {
                            requestBuilder?.centerCrop()
                        }
                        ImageConfig.CIRCLE -> {
                            requestBuilder?.circleCrop()
                        }
                        else -> {
                        }
                    }
                }
            })

            if (config.mBorderRadius > 0) {
                requestBuilder?.apply(bitmapTransform(RoundedCornersTransformation(config.mBorderRadius, 0)))
            }

            config.mImageView?.let {
                requestBuilder?.into(it)
            }
        }

        fun clearData() {
            async {

                GlideApp.get(Utils.getApp()).clearDiskCache()
                GlideApp.get(Utils.getApp()).clearMemory()
            }
        }
    }
}
