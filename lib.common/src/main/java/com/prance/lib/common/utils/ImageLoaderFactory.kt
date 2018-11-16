package com.prance.lib.common.utils

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.prance.lib.common.utils.GlideOptions.bitmapTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
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

            var requestBuilder: GlideRequest<*>?

            if (config.mIsGif) {
                requestBuilder = mGlideRequests?.asGif()
            }

            requestBuilder = mGlideRequests?.load(config.mUrl)

            config.mBitmapTypes.forEach { i ->
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
            }

            if (config.mBorderRadius > 0) {
                requestBuilder?.apply(bitmapTransform(RoundedCornersTransformation(config.mBorderRadius, 0)))
            }

            config.mImageView?.let {
                requestBuilder?.into(it)
            }
        }

    }
}
