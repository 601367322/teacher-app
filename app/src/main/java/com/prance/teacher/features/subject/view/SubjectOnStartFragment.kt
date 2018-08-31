package com.prance.teacher.features.subject.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.View
import cn.sunars.sdk.SunARS
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.sunvote.platform.UsbManagerImpl
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.danmutest.CenteredImageSpan
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.model.KeyPadResult
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_subject_on_start.*
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import java.util.*

/**
 * 开始答题
 */
class SubjectOnStartFragment : BaseFragment() {

    val mResult = mutableListOf<KeyPadResult>()
    var mQuestion: ClassesDetailFragment.Question? = null

    var KEY_ENENT_HANDLER_WHAT = 1

    lateinit var mDanmuContext: DanmakuContext

    lateinit var mDanmuParser: BaseDanmakuParser

    var lastDanmuTime = 0L
    var danmuInterval = 2000L

    companion object {

        fun forQuestion(question: ClassesDetailFragment.Question): SubjectOnStartFragment {
            var fragment = SubjectOnStartFragment()
            val bundle = Bundle()
            bundle.putSerializable(SubjectActivity.QUESTION, question)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_subject_on_start

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mQuestion = arguments?.getSerializable(SubjectActivity.QUESTION) as ClassesDetailFragment.Question?

        //初始化弹幕组件
        initDanmu()

        UsbManagerImpl.baseStation.sn?.let {
            mQuestion?.run {
                //基站开始发送题目
                SunARS.voteStart(type!!, param)
            }
        }

        ClassesDetailFragment.mStudentList?.let {
            powerProgressbar.max = it.size
        }
        if (BuildConfig.DEBUG) {
//            powerProgressbar.max = 32
        }

        mSunVoteServicePresenter.bind()
    }

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy {
        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

            override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
                val keyId = generateKeyPadId(KeyID)
                when (iMode) {
                    SunARS.KeyResult_info -> {
                        sInfo?.let {
                            Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult(keyId, sInfo, System.currentTimeMillis())).sendToTarget()
                        }
                    }
                }
            }
        })
    }

    val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun dispatchMessage(msg: Message) {
            when (msg.what) {
                KEY_ENENT_HANDLER_WHAT -> {
                    val keyPadResult = msg.obj as KeyPadResult

                    //去重
                    for (result in mResult) {
                        if (result.clickerId == keyPadResult.clickerId) {
                            return
                        }
                    }

                    var studentEntity: StudentsEntity? = null
                    //根据keyId去找到对应的学生信息
                    ClassesDetailFragment.mStudentList?.let {
                        for (item in it) {
                            if (keyPadResult.clickerId == item.getClicker()?.number) {
                                studentEntity = item
                            }
                        }
                    }
                    //如果学生信息没有找到，则放弃处理
                    if (BuildConfig.DEBUG) {
//                        if (studentEntity == null) {
//                            studentEntity = StudentsEntity("假数据", "")
//                        }
                    }
                    if (studentEntity == null) {
                        return
                    }
                    mResult.add(keyPadResult)

                    //判断答案是否正确
                    if (keyPadResult.answer != mQuestion?.result) {
                        return
                    }

                    //添加弹幕
                    addDanmakuShowTextAndImage(studentEntity!!)

                    powerProgressbar.progress += 1
                }
            }
            super.dispatchMessage(msg)
        }
    }

    private fun initDanmu() {
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
                danmu?.start()
            }
        })
        danmu.prepare(mDanmuParser, mDanmuContext)
        danmu.enableDanmakuDrawingCache(true)
    }

    private fun addDanmakuShowTextAndImage(studentsEntity: StudentsEntity) {

        var delay = 0L
        val diffTime = System.currentTimeMillis() - lastDanmuTime
        //计算弹幕出现的时间，间隔danmuInterval
        if (diffTime in 1..(danmuInterval - 1)) {
            delay = danmuInterval - diffTime
        } else if (diffTime < 0) {
            delay = lastDanmuTime + danmuInterval - System.currentTimeMillis()
        }
        lastDanmuTime = System.currentTimeMillis() + delay
        danmu.postDelayed({
            //加载头像
            GlideApp.with(this)
                    .asDrawable()
                    .load(studentsEntity.head)
                    .error(R.drawable.default_avatar_boy)
                    .override(100, 100)
                    .transform(CropCircleTransformation())
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            showDanmu(resource)
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            errorDrawable?.let { showDanmu(it) }
                        }

                        private fun showDanmu(resource: Drawable) {
                            try {
                                val danmaku = mDanmuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
                                //绘制头像
                                resource.setBounds(0, 0, 100, 100)
                                val spannable = createSpannable(resource, studentsEntity.name)

                                danmaku.text = spannable
                                danmaku.padding = 5
                                danmaku.priority = 1  // 一定会显示, 一般用于本机发送的弹幕
                                danmaku.isLive = false
                                danmaku.time = danmu.currentTime
                                context?.run {
                                    danmaku.textSize = resources.getDimensionPixelOffset(R.dimen.m25_0).toFloat()
                                }
                                danmaku.textColor = Color.RED
                                danmaku.textShadowColor = 0 // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                                danmu.addDanmaku(danmaku)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    })
        }, delay)
    }


    private fun createSpannable(drawable: Drawable, name: String): SpannableStringBuilder {
        val text = "bitmap"
        val spannableStringBuilder = SpannableStringBuilder(text)
        val span = CenteredImageSpan(drawable)
        spannableStringBuilder.setSpan(span, 0, text.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableStringBuilder.append("$name 做对啦~")
        return spannableStringBuilder
    }


    override fun onDestroy() {
        super.onDestroy()
        //停止发送
        SunARS.voteStop()
        //释放弹幕资源
        if (danmu != null) {
            danmu.release()
        }
        //解绑答题器Service
        mSunVoteServicePresenter.unBind()
    }


}