package com.prance.teacher.features.danmutest

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_danmu.*
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import java.util.HashMap
import java.util.concurrent.TimeUnit

class DanmuTestActivity : BaseActivity() {

    override fun fragment(): BaseFragment? = null

    override fun layoutId(): Int = R.layout.activity_danmu

    lateinit var mDanmuContext: DanmakuContext

    lateinit var mDanmuParser: BaseDanmakuParser

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        // 设置最大显示行数
        val maxLinesPair = mutableMapOf(
                BaseDanmaku.TYPE_SCROLL_RL to 1
        )

        // 设置是否禁止重叠
        val overlappingEnablePair = mutableMapOf(
                BaseDanmaku.TYPE_SCROLL_RL to true,
                BaseDanmaku.TYPE_FIX_TOP to true
        )

        mDanmuContext = DanmakuContext.create()
        mDanmuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3F)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setCacheStuffer(SpannedCacheStuffer(), null)
                .setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40)
        mDanmuParser = object : BaseDanmakuParser() {
            override fun parse(): Danmakus {
                return Danmakus()
            }
        }
        danmu.setCallback(object : DrawHandler.Callback {
            override fun updateTimer(timer: DanmakuTimer) {}
            override fun drawingFinished() {}
            override fun danmakuShown(danmaku: BaseDanmaku) {}
            override fun prepared() {
                danmu.start()
            }
        })
        danmu.prepare(mDanmuParser, mDanmuContext)
        danmu.enableDanmakuDrawingCache(true)

        Flowable.interval(2, TimeUnit.SECONDS)
                .subscribe({
                    addDanmaKuShowTextAndImage()
                })
    }

    private fun addDanmaKuShowTextAndImage() {
        val danmaku = mDanmuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        val drawable = resources.getDrawable(R.drawable.ic_launcher)
        drawable.setBounds(0, 0, 100, 100)
        val spannable = createSpannable(drawable)
        danmaku.text = spannable
        danmaku.padding = 5
        danmaku.priority = 1  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = false
        danmaku.time = danmu.currentTime
        danmaku.textSize = 25f * (mDanmuParser.displayer.density - 0.6f)
        danmaku.textColor = Color.RED
        danmaku.textShadowColor = 0 // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmu.addDanmaku(danmaku)
    }


    private fun createSpannable(drawable: Drawable): SpannableStringBuilder {
        val text = "bitmap"
        val spannableStringBuilder = SpannableStringBuilder(text)
        val span = CenteredImageSpan(drawable)
        spannableStringBuilder.setSpan(span, 0, text.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableStringBuilder.append("图文混排")
        return spannableStringBuilder
    }

}