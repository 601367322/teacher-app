package com.prance.teacher.weight

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class GameOver(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {

    init {
        setImageResource(R.drawable.game_over)
    }

    fun start(onAnimEnd: (() -> Unit)) {
        visible()
        val animatorX = ObjectAnimator.ofFloat(this, AnimUtil.SCALEX, 0.2F, 1F)
        val animatorY = ObjectAnimator.ofFloat(this, AnimUtil.SCALEY, 0.2F, 1F)

        val animationSet = AnimatorSet()
        animationSet.playTogether(animatorX, animatorY)
        animationSet.interpolator = DecelerateInterpolator()
        animationSet.duration = 1000
        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                Flowable.timer(1,TimeUnit.SECONDS)
                        .mySubscribe {
                            onAnimEnd.invoke()
                        }
            }
        })
        animationSet.start()
    }
}