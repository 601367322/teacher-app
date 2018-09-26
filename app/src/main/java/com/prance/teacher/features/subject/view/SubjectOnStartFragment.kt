package com.prance.teacher.features.subject.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.model.KeyPadResult
import com.prance.teacher.features.subject.view.danmu.DanmuAnimGLView
import com.spark.teaching.answertool.usb.model.ReceiveAnswer
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_subject_on_start.*

/**
 * 开始答题
 */
class SubjectOnStartFragment : BaseFragment() {

    val mResult = mutableListOf<KeyPadResult>()
    var mQuestion: ClassesDetailFragment.Question? = null

    var KEY_ENENT_HANDLER_WHAT = 1
    var SHOW_DANMU_WHAT = 2

    var lastDanmuTime = 0L

    var danmuInterval = 3000L

    var boxLightAnim: ValueAnimator? = null

    var mDanmuView: DanmuAnimGLView? = null

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

        //设置进度条最大人数
        mQuestion?.let {
            powerProgressbar.max = it.signStudents?.size ?: 0
        }

        if (BuildConfig.DEBUG) {
//            powerProgressbar.max = 32
        }

        mSparkServicePresenter.bind()
    }

    private val mSparkServicePresenter: SparkServicePresenter by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onAnswer(answer: ReceiveAnswer) {
                val keyId = answer.uid.toString()
                Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult(keyId, answer.answer, System.currentTimeMillis())).sendToTarget()
            }

            override fun onServiceConnected() {
                SparkService.mUsbSerialNum?.let {
                    mQuestion?.run {
                        //基站开始发送题目
                        LogUtils.d("开始发送题目")
                        mSparkServicePresenter.sendQuestion(getQuestionType())
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
                            studentEntity = StudentsEntity(1, "假数据", "https://upload.jianshu.io/users/upload_avatars/2897594/eb89b4338b1a.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96")
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
                            try {
                                boxLight.visible()
                                boxLightAnim = ObjectAnimator.ofFloat(boxLight, AnimUtil.ROTATION, 0F, 360F).setDuration(1000)
                                boxLightAnim!!.interpolator = LinearInterpolator()
                                boxLightAnim!!.repeatCount = Animation.INFINITE
                                boxLightAnim!!.repeatMode = ValueAnimator.RESTART
                                boxLightAnim!!.start()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
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
                            .error(R.drawable.danmu_default_avatar)
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

                                            if (activity == null) {
                                                return@postDelayed
                                            }

                                            try {
                                                danmu.mDanmuManager?.add(resource, studentsEntity.name)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }, delay)


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

    override fun onDestroy() {
        super.onDestroy()

        mHandler.removeMessages(KEY_ENENT_HANDLER_WHAT)
        mHandler.removeMessages(SHOW_DANMU_WHAT)

        //停止宝箱灯光动画
        boxLightAnim?.cancel()
        boxLightAnim = null

        //停止发送
        mSparkServicePresenter.stopAnswer()

        //解绑答题器Service
        mSparkServicePresenter.unBind()
    }


}