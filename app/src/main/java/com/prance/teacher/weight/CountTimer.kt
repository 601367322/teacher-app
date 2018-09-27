package com.prance.teacher.weight

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.media.SoundPool
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.getInflate
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import com.prance.teacher.utils.SoundUtils
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_classes.view.*
import kotlinx.android.synthetic.main.widget_count_timer.view.*
import java.util.concurrent.TimeUnit

class CountTimer(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    var timeRes: MutableMap<String, Int> = mutableMapOf(
            "0" to R.drawable.count_time_0,
            "1" to R.drawable.count_time_1,
            "2" to R.drawable.count_time_2,
            "3" to R.drawable.count_time_3,
            "4" to R.drawable.count_time_4,
            "5" to R.drawable.count_time_5,
            "6" to R.drawable.count_time_6,
            "7" to R.drawable.count_time_7,
            "8" to R.drawable.count_time_8,
            "9" to R.drawable.count_time_9
    )
    var lastTimeRes: MutableMap<String, Int> = mutableMapOf(
            "1" to R.drawable.count_time_01,
            "2" to R.drawable.count_time_02,
            "3" to R.drawable.count_time_03,
            "4" to R.drawable.count_time_04,
            "5" to R.drawable.count_time_05
    )

    var mDisposable: Disposable? = null

    var count = 0

    init {
        addView(getInflate(this, R.layout.widget_count_timer))
    }

    private fun convertTextToBitmap(countNum: Int, preCountNum: Int = 5, text: String): MutableList<Int> {
        val list = mutableListOf<Int>()
        for (i in text) {
            if (countNum <= preCountNum) {
                list.add(this.lastTimeRes[i.toString()]!!)
            } else {
                list.add(this.timeRes[i.toString()]!!)
            }
        }
        return list
    }

    fun start(countNum: Int, preCountNum: Int = 5, onTimeEnd: (() -> Unit)? = null) {
        visible()
        this.count = countNum
        mDisposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(countNum.toLong())
                .mySubscribe {
                    val scores = convertTextToBitmap(this.count, preCountNum, this.count.toString())
                    timeLayout.removeAllViews()

                    var animationSet: AnimatorSet? = null

                    for (score in scores) {
                        val image = ImageView(context)
                        image.layoutParams = LinearLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.m45_0), resources.getDimensionPixelOffset(R.dimen.m70_0))
                        image.setImageResource(score)
                        timeLayout.addView(image)

                        if (this.count <= preCountNum) {
                            val animatorA = ObjectAnimator.ofInt(image, AnimUtil.ALPHA, 255, 0)
                            val animatorX = ObjectAnimator.ofFloat(image, AnimUtil.SCALEX, 1.5F, 0.5F)
                            val animatorY = ObjectAnimator.ofFloat(image, AnimUtil.SCALEY, 1.5F, 0.5F)

                            animationSet = AnimatorSet()
                            animationSet.playTogether(animatorA, animatorX, animatorY)
                            animationSet.interpolator = DecelerateInterpolator()
                            animationSet.duration = 1000

                            if(this.count == preCountNum){
                                SoundUtils.play("five_count_time")
                            }
                        }
                    }

                    this.count--

                    if (this.count < 1) {
                        if (animationSet != null) {
                            animationSet.addListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    end()
                                    onTimeEnd?.invoke()
                                }
                            })
                        }else {
                            end()
                            onTimeEnd?.invoke()
                        }
                    }
                    animationSet?.start()
                }
    }

    fun end() {
        mDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}