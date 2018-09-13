package com.prance.teacher.features.subject.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.BitmapFactory.decodeResource
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import cn.sunars.sdk.SunARS
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
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
import master.flame.danmaku.ui.widget.DanmakuView

/**
 * 开始答题
 */
class SubjectOnStartFragment : BaseFragment() {

    val mResult = mutableListOf<KeyPadResult>()
    var mQuestion: ClassesDetailFragment.Question? = null

    var KEY_ENENT_HANDLER_WHAT = 1
    var SHOW_DANMU_WHAT = 2

    lateinit var mDanmuContext: DanmakuContext

    lateinit var mDanmuParser: BaseDanmakuParser

    var lastDanmuTime = 0L

    var danmuInterval = 3000L

    var boxLightAnim: ValueAnimator? = null

    var mDanmuView: DanmakuView? = null

    var doubleScore = false

    lateinit var avatarBackground: Bitmap

    companion object {

        fun forQuestion(question: ClassesDetailFragment.Question): SubjectOnStartFragment {
            val fragment = SubjectOnStartFragment()
            val bundle = Bundle()
            bundle.putSerializable(SubjectActivity.QUESTION, question)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_subject_on_start

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mQuestion = arguments?.getSerializable(SubjectActivity.QUESTION) as ClassesDetailFragment.Question?

        avatarBackground = BitmapFactory.decodeResource(resources, R.drawable.subject_danmu_avatar_bg)

        mDanmuView = rootView.findViewById(R.id.danmu)

        //初始化弹幕组件
        initDanmu()

        UsbManagerImpl.baseStation.sn?.let {
            mQuestion?.run {
                //基站开始发送题目
                SunARS.voteStart(type!!, param)
            }
        }

        //设置进度条最大人数
        mQuestion?.let {
            powerProgressbar.max = it.signStudents?.size ?: 0
        }


        if (BuildConfig.DEBUG) {
//            powerProgressbar.max = 32
        }

        mSunVoteServicePresenter.bind()
    }

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy {
        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

            var answerList = mutableListOf<String>()

            override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
                //防止重复提交
                if (answerList.contains(KeyID)) {
                    return
                }
                answerList.add(KeyID)

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
                    //答题器事件
                    val keyPadResult = msg.obj as KeyPadResult

                    var studentEntity: StudentsEntity? = ClassesDetailFragment.checkIsSignStudent(mQuestion?.signStudents, keyPadResult.clickerId)

                    //如果学生信息没有找到，则放弃处理
                    if (BuildConfig.DEBUG) {
                        if (studentEntity == null) {
                            studentEntity = StudentsEntity(1, "假数据", "")
                        }
                    }
                    if (studentEntity == null) {
                        return
                    }
                    mResult.add(keyPadResult)

                    answerNumber.text = (answerNumber.text.toString().toInt() + 1).toString()

                    //判断答案是否正确
                    if (keyPadResult.answer != mQuestion?.result) {
                        return
                    }

                    //添加弹幕
                    sendMessage(Message.obtain(this, SHOW_DANMU_WHAT, studentEntity))

                    powerProgressbar.progress += 1

                    //进度大于70%，开启宝箱
                    if ((powerProgressbar.progress.toFloat() / powerProgressbar.max.toFloat()) * 100 > 70) {

                        doubleScore = true

                        //宝箱打开动画
                        val animationDrawable = box.drawable as AnimationDrawable
                        animationDrawable.start()
                        //1秒后打开宝箱灯光
                        postDelayed({
                            boxLight.visible()
                            boxLightAnim = ObjectAnimator.ofFloat(boxLight, AnimUtil.ROTATION, 0F, 360F).setDuration(1000)
                            boxLightAnim!!.interpolator = LinearInterpolator()
                            boxLightAnim!!.repeatCount = Animation.INFINITE
                            boxLightAnim!!.repeatMode = ValueAnimator.RESTART
                            boxLightAnim!!.start()
                        }, 1000)
                    }
                }
                SHOW_DANMU_WHAT -> {
                    //显示弹幕
                    val studentsEntity = msg.obj as StudentsEntity
                    val avatarHeight = resources.getDimensionPixelOffset(R.dimen.m104_0)
                    //加载头像
                    GlideApp.with(this@SubjectOnStartFragment)
                            .asBitmap()
                            .load(studentsEntity.head)
                            .error(R.drawable.default_avatar_boy)
                            .override(avatarHeight, avatarHeight)
                            .transform(CropCircleTransformation())
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    showDanmu(resource)
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    errorDrawable?.let { showDanmu((it as BitmapDrawable).bitmap) }
                                }

                                var inited = false

                                private fun showDanmu(resource: Bitmap) {
                                    if (activity == null || inited) {
                                        return
                                    }
                                    inited = true
                                    try {

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

                                            if(activity == null){
                                                return@postDelayed
                                            }

                                            try {
                                                val danmaku = mDanmuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)

                                                //画头像
                                                val avatarBgHeight = resources.getDimensionPixelOffset(R.dimen.m172_0)
                                                val bitmap = Bitmap.createBitmap(avatarBgHeight, avatarBgHeight, Bitmap.Config.ARGB_4444)
                                                val canvas = Canvas(bitmap)
                                                val bgMatrix = Matrix()
                                                bgMatrix.setScale(avatarBgHeight.toFloat() / avatarBackground.height.toFloat(), avatarBgHeight.toFloat() / avatarBackground.height.toFloat())
                                                canvas.drawBitmap(avatarBackground, bgMatrix, null)
                                                val resourceMatrix = Matrix()
                                                resourceMatrix.postScale(avatarHeight.toFloat() / resource.height.toFloat(), avatarHeight.toFloat() / resource.height.toFloat())
                                                resourceMatrix.postTranslate((avatarBgHeight - avatarHeight) / 2f, (avatarBgHeight - avatarHeight) / 2f)
                                                canvas.drawBitmap(resource, resourceMatrix, null)

                                                val drawable = BitmapDrawable(bitmap)
                                                drawable.setBounds(0, 0, avatarBgHeight, avatarBgHeight)
                                                //绘制头像
                                                val spannable = createSpannable(drawable, studentsEntity.name)

                                                danmaku.text = spannable
                                                danmaku.padding = 5
                                                danmaku.priority = 1  // 一定会显示, 一般用于本机发送的弹幕
                                                danmaku.isLive = false
                                                danmaku.time = danmu.currentTime
                                                context?.run {
                                                    danmaku.textSize = resources.getDimensionPixelOffset(R.dimen.m50_0).toFloat()
                                                }
                                                danmaku.textColor = Color.parseColor("#512B90")
                                                danmaku.textShadowColor = 0 // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                                                danmu.addDanmaku(danmaku)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        },delay)


                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            })
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

        mHandler.removeMessages(KEY_ENENT_HANDLER_WHAT)
        mHandler.removeMessages(SHOW_DANMU_WHAT)

        //停止宝箱灯光动画
        boxLightAnim?.cancel()
        boxLightAnim = null

        //停止发送
        SunARS.voteStop()

        //释放弹幕资源
        mDanmuView?.release()

        //解绑答题器Service
        mSunVoteServicePresenter.unBind()
    }


}