package com.prance.teacher.features.redpackage.view.red

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.DEFAULT_ALPHA
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.DEFAULT_WIDTH
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.lines
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.DEFAULT_SCORE
import com.prance.teacher.features.students.model.StudentEntity
import com.prance.teacher.utils.SoundUtils
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class RedPackage {

    var screenHeight: Int = Utils.getApp().resources.displayMetrics.heightPixels
    //屏幕宽高
    private var screenWidth: Int = Utils.getApp().resources.displayMetrics.widthPixels

    //位置
    var x: Int
    var y: Int

    var titleX = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m120_0)
    var titleY = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m150_0)

    //创建时间
    var createTime: Long

    //透明度
    var alpha: Int

    //宽高
    var width: Int
    var height: Int

    //标题
    var title: String

    var translationAnimator: ValueAnimator? = null
    var hideAnimator: ValueAnimator? = null

    var bitmap: Bitmap
    //气泡
    var bubble: Bubble? = null
    //标题abcd
    var redPackageTitle: Bitmap
    //当前的红包图Gif
    var redPackage: Bitmap
    //所有红包图Gif
    var redPackageArray: MutableList<Bitmap>

    var tipBitmap: Bitmap

    var scoreTip: ScoreTip? = null

    //被抢的状态
    var state = RedPackageStatus.CANNOTGRAB

    var context: Context

    //第几泳道
    var lineNum: Int

    //大红包
    var big = false

    //分数
    var score = 0

    //红包积分数字图
    var scoreBitmaps: MutableMap<String, Bitmap> = mutableMapOf()

    var position: Int = 0

    var disposable: Disposable? = null

    //快速红包
    var fast: Boolean = false

    constructor(
            context: Context,
            width: Int,
            height: Int,
            title: String,
            lineNum: Int,
            big: Boolean,
            score: Int,
            bubble: Bubble,
            redPackageTitle: Bitmap,
            redPackageArray: MutableList<Bitmap>,
            tipBitmap: Bitmap,
            scoreBitmaps: MutableMap<String, Bitmap>,
            fast: Boolean) {

        this.context = context
        this.x = getRedPackageStartX(lineNum).toInt()
        this.y = -height
        this.createTime = System.currentTimeMillis()
        this.alpha = DEFAULT_ALPHA
        this.width = width
        this.height = height
        this.title = title
        this.big = big
        this.score = score
        this.bubble = bubble
        this.redPackageTitle = redPackageTitle
        this.redPackage = redPackageArray[0]
        this.redPackageArray = redPackageArray
        this.scoreBitmaps = scoreBitmaps
        this.bitmap = redPackageArray[0]
        this.tipBitmap = tipBitmap
        this.lineNum = lineNum
        this.fast = fast
    }

    fun startFall() {
        if (translationAnimator == null) {

            disposable = Flowable.interval(133, TimeUnit.MILLISECONDS)
                    .subscribe {
                        if (position < redPackageArray.size) {
                            redPackage = redPackageArray[position]
                            position++
                            if (position >= redPackageArray.size) {
                                position = 0
                            }
                        }
                    }

            translationAnimator = ObjectAnimator.ofInt(-height, screenHeight).setDuration(if (fast) RedPackageManager.fastTranslationDurationTime else RedPackageManager.translationDurationTime)
            translationAnimator!!.interpolator = LinearInterpolator()
            translationAnimator!!.addUpdateListener {
                y = it.animatedValue.toString().toInt()

                if (y >= height) {
                    //全部出来之后才可以抢
                    if (state == RedPackageStatus.CANNOTGRAB)
                        state = RedPackageStatus.CANGRAB
                }

                if (BuildConfig.DEBUG) {
//                    if (y > 300 && hideAnimator == null) {
//                        destroy(StudentScore(StudentEntity(mutableListOf("百度", "笑话", "呵呵")[(Math.random() * 2).toInt()], "https://gd2.alicdn.i4/698598873/O1CN012FPskizp9t8Ufvn_!!698598873.jpg_400x400.jpg"), 2, 2))
//                    }
                }
            }
            translationAnimator!!.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    //如果该红包未被抢，则越过底部之后变为释放状态
                    if (state != RedPackageStatus.GRAB) {
                        state = RedPackageStatus.FREE

                        stopEverything()
                    }
                }
            })
            translationAnimator!!.start()

            bubble?.startAnim()
        }
    }

    fun destroy() {
        //取消下落动画
        translationAnimator?.cancel()
    }

    fun destroy(studentScore: StudentScore) {
        //设置为已被抢状态
        state = RedPackageStatus.GRAB

        try {
            if (!(context as Activity).isDestroyed) {
                SoundUtils.play("red_package_get")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //生成抢到红包提示
        scoreTip = ScoreTip(
                context,
                x,
                y,
                width,
                height,
                studentScore.student,
                if (big) DEFAULT_SCORE * 2 else DEFAULT_SCORE,
                tipBitmap,
                scoreBitmaps)

        if (hideAnimator == null) {
            hideAnimator = ObjectAnimator.ofInt(alpha, 0).setDuration(RedPackageManager.alphaDurationTime)
            hideAnimator!!.interpolator = LinearInterpolator()
            hideAnimator!!.addUpdateListener {
                alpha = it.animatedValue.toString().toInt()
            }
            hideAnimator!!.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    //取消下落动画
                    translationAnimator?.cancel()

                    //动画结束之后变为释放状态
                    state = RedPackageStatus.FREE

                    stopEverything()
                }
            })
            hideAnimator!!.start()
        }
    }

    /**
     * 红包开始的X
     */
    private fun getRedPackageStartX(lineNumber: Int): Float {
        val startX = getLineWidth() * lineNumber
        return ((getLineWidth() - DEFAULT_WIDTH) / 2) + startX
    }

    /**
     * 泳道的宽度
     */
    private fun getLineWidth(): Float {
        return screenWidth.toFloat() / lines.toFloat()
    }

    private fun stopEverything() {
        disposable?.dispose()
        bubble?.destroy()
        bubble = null
    }
}