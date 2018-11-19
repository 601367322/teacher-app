package com.prance.teacher.features.subject.view

import android.animation.*
import android.graphics.*
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.Constants.QUESTION
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.R.id.danmu
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.students.model.StudentEntity
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

    var boxLightAnim: ValueAnimator? = null

    var doubleScore = false

    var mRankNames = mutableListOf<TextView>()

    var rankBackgrounds = mutableListOf(R.drawable.subject_onstart_rank_bg_1,
            R.drawable.subject_onstart_rank_bg_2,
            R.drawable.subject_onstart_rank_bg_3,
            R.drawable.subject_onstart_rank_bg_4,
            R.drawable.subject_onstart_rank_bg_5,
            R.drawable.subject_onstart_rank_bg_6,
            R.drawable.subject_onstart_rank_bg_7,
            R.drawable.subject_onstart_rank_bg_8,
            R.drawable.subject_onstart_rank_bg_9,
            R.drawable.subject_onstart_rank_bg_10)

    lateinit var avatarBackground: Bitmap

    companion object {

        fun forQuestion(question: ClassesDetailFragment.Question): SubjectOnStartFragment {
            val fragment = SubjectOnStartFragment()
            val bundle = Bundle()
            bundle.putSerializable(QUESTION, question)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_subject_on_start

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mQuestion = arguments?.getSerializable(QUESTION) as ClassesDetailFragment.Question?

        avatarBackground = BitmapFactory.decodeResource(resources, R.drawable.subject_danmu_avatar_bg)

        //设置进度条最大人数
        mQuestion?.let {
            powerProgressbar.max = it.signStudents?.size ?: 0

            when (it.getQuestionType()) {
                SparkService.QuestionType.SINGLE -> {
                    questionTypeLayout1.visibility = View.VISIBLE
                }
                SparkService.QuestionType.MULTI -> {
                    questionTypeLayout2.visibility = View.VISIBLE
                }
                SparkService.QuestionType.YES_OR_NO -> {
                    questionTypeLayout3.visibility = View.VISIBLE
                }
            }
        }

        if (BuildConfig.DEBUG) {

            Handler().postDelayed({
                Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult("123456", "A", System.currentTimeMillis())).sendToTarget()
            }, 1000)
            Handler().postDelayed({
                Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult("123456", "A", System.currentTimeMillis())).sendToTarget()
            }, 1150)
            Handler().postDelayed({
                Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult("123456", "A", System.currentTimeMillis())).sendToTarget()
            }, 1200)
            Handler().postDelayed({
                Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult("123456", "A", System.currentTimeMillis())).sendToTarget()
            }, 1350)
            Handler().postDelayed({
                Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult("123456", "A", System.currentTimeMillis())).sendToTarget()
            }, 1400)
            Handler().postDelayed({
                Message.obtain(mHandler, KEY_ENENT_HANDLER_WHAT, KeyPadResult("123456", "A", System.currentTimeMillis())).sendToTarget()
            }, 1550)
        }

        for (i in 0 until 10) {
            generateView(i)
        }

        subjectBottomLayout.post {
            ObjectAnimator.ofFloat(subjectBottomLayout, AnimUtil.TRANSLATIONY, resources.getDimensionPixelOffset(R.dimen.m190_0).toFloat(), 0f).start()
        }

        mSparkServicePresenter.bind()
    }


    private fun generateView(index: Int) {

        val relativeLayout = RelativeLayout(context)
        relativeLayout.visibility = View.GONE

        val image = ImageView(context)
        val imageLayoutParams = RelativeLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.m204_0), resources.getDimensionPixelOffset(R.dimen.m60_0))
        image.setImageResource(rankBackgrounds[index])
        image.layoutParams = imageLayoutParams

        val text = TextView(context)
        val textLayoutParams = RelativeLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.m116_0), RelativeLayout.LayoutParams.WRAP_CONTENT)
        textLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        textLayoutParams.leftMargin = resources.getDimensionPixelOffset(R.dimen.m11_0)
        text.maxLines = 1
        text.ellipsize = TextUtils.TruncateAt.END
        text.setTextColor(Color.WHITE)
        text.setTextSize(resources.getDimensionPixelOffset(R.dimen.m12_0).toFloat())
        text.layoutParams = textLayoutParams

        relativeLayout.addView(image)
        relativeLayout.addView(text)

        rankLayout.addView(relativeLayout)

        mRankNames.add(text)

        rankLayout.postDelayed({
            val anim = ObjectAnimator.ofFloat(relativeLayout, AnimUtil.TRANSLATIONX, resources.getDimensionPixelOffset(R.dimen.m300_0).toFloat(), 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    relativeLayout.visibility = View.VISIBLE
                }
            })
            anim.start()
        }, (index * 50).toLong())
    }

    private val mSparkServicePresenter: SparkServicePresenter by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onAnswer(answer: ReceiveAnswer) {
                val keyId = answer.uid.toString()

                //提交反馈
                mSparkServicePresenter.sendData("已提交", keyId)

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

                    var studentEntity: StudentEntity? = ClassesDetailFragment.checkIsSignStudent(mQuestion?.signStudents, keyPadResult.clickerId)

                    //如果学生信息没有找到，则放弃处理
                    if (BuildConfig.DEBUG) {
                        if (studentEntity == null) {
                            studentEntity = StudentEntity(1, "假数据01", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3376193873,2090747280&fm=27&gp=0.jpg")
                        }
                    }
                    if (studentEntity == null) {
                        return
                    }
                    mResult.add(keyPadResult)

                    if (mResult.size < mRankNames.size) {
                        var textView = mRankNames[mResult.size - 1]
                        textView.text = studentEntity.name

                        var anim = AnimatorSet()
                        anim.playTogether(
                                ObjectAnimator.ofFloat(textView, AnimUtil.ALPHA, 0f, 255f),
                                ObjectAnimator.ofFloat(textView, AnimUtil.SCALEX, 1.2f, 1.0f),
                                ObjectAnimator.ofFloat(textView, AnimUtil.SCALEY, 1.2f, 1.0f)
                        )
                        anim.start()
                    }

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
                    val studentsEntity = msg.obj as StudentEntity
                    try {
                        val height = box.measuredHeight
                        val width = box.measuredWidth
                        val position = IntArray(2)
                        box.getLocationInWindow(position)
                        danmu?.mDanmuManager?.add(studentsEntity, width.toFloat() / 2f + position[0], height.toFloat() / 2f + position[1])
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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