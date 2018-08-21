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
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.sunvote.platform.UsbManagerImpl
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.danmutest.CenteredImageSpan
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.model.KeyPadResult
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

        UsbManagerImpl.baseStation.sn?.let {
            mQuestion?.run {
                //基站开始发送题目
                SunARS.voteStart(type!!, param)
            }
        }

        ClassesDetailFragment.mStudentList?.let {
            powerProgressbar.max = it.size
        }

        mSunVoteServicePresenter.bind()
    }

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy {
        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

            /**
             * 防止初始化的时候，UsbManagerImpl.baseStation是空的，当重新绑定Service的时候，再执行发一遍题目
             * SunARS.voteStart(type!!, param) 这行代码，只执行一次，要么初始化时执行，要么绑定完Service，打开usb后执行
             */
            override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
                super.onConnectEventCallBack(iBaseID, iMode, sInfo)
                if (sInfo == SunARS.BaseStation_Connected) {
                    mQuestion?.run {
                        //基站开始发送题目
                        SunARS.voteStart(type!!, param)
                    }
                }
            }

            override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
                val keyId = generateKeyPadId(KeyID)
                when (iMode) {
                    SunARS.KeyResult_info -> {
                        sInfo?.let {
                            Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult(keyId, sInfo, Date().time)).sendToTarget()
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
                    if (studentEntity == null) {
                        return
                    }
                    mResult.add(keyPadResult)

                    addDanmakuShowTextAndImage()
                }
            }
            super.dispatchMessage(msg)
        }
    }

    private fun initDanmu(){
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
    }

    private fun addDanmakuShowTextAndImage(studentsEntity: StudentsEntity) {
//        GlideApp.with(this)
//                .asBitmap()
//                .load(studentsEntity.head)
//                .override()
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


    override fun onDestroy() {
        super.onDestroy()
        //停止发送
        SunARS.voteStop()

        mSunVoteServicePresenter.unBind()
    }


}