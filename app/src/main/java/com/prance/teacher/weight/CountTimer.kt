package com.prance.teacher.weight

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
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
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.widget_count_timer.view.*
import java.util.concurrent.TimeUnit

class CountTimer(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    //红包积分数字图
    var scoreBitmaps: MutableMap<String, Int> = mutableMapOf(
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

    var mDisposable: Disposable? = null

    var count = 0

    init {
        addView(getInflate(this, R.layout.widget_count_timer))
    }

    private fun convertTextToBitmap(text: String): MutableList<Int> {
        val list = mutableListOf<Int>()
        for (i in text) {
            list.add(this.scoreBitmaps[i.toString()]!!)
        }
        return list
    }

    fun start(countNum: Int, preCountNum: Int = 5, onTimeEnd: (() -> Unit)? = null) {
        visible()
        this.count = countNum
        mDisposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(countNum.toLong())
                .mySubscribe {
                    val scores = convertTextToBitmap(this.count.toString())
                    timeLayout.removeAllViews()
                    for (score in scores) {
                        val image = ImageView(context)
                        image.layoutParams = LinearLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.m45_0), resources.getDimensionPixelOffset(R.dimen.m70_0))
                        image.setImageResource(score)
                        timeLayout.addView(image)

                        if (this.count <= preCountNum) {
                            val animatorA = ObjectAnimator.ofInt(image, AnimUtil.ALPHA, 255, 0)
                            val animatorX = ObjectAnimator.ofFloat(image, AnimUtil.SCALEX, 1.5F, 0.5F)
                            val animatorY = ObjectAnimator.ofFloat(image, AnimUtil.SCALEY, 1.5F, 0.5F)

                            val animationSet = AnimatorSet()
                            animationSet.playTogether(animatorA, animatorX, animatorY)
                            animationSet.interpolator = DecelerateInterpolator()
                            animationSet.duration = 1000
                            animationSet.start()
                        }
                    }

                    this.count--

                    if (this.count < 1) {
                        end()
                        onTimeEnd?.invoke()
                    }
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