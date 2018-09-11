package com.prance.teacher.features.pk.rocket

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.PointF
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import com.chillingvan.canvasgl.ICanvasGL
import com.prance.teacher.features.pk.view.ICountTimeListener
import org.greenrobot.eventbus.EventBus
import java.util.*

 open class BaseRocket(
        var startPoint: PointF,
        var targetPoint: PointF,
        var bitmap: Bitmap) : CollisionListener, ICountTimeListener {

    var wallTop = BaseWall.BaseWallY(0F)
    var wallLeft = BaseWall.BaseWallX(0F)
    var mBaseWallBottom: BaseWall? = null
    var mBaseWallRight: BaseWall? = null

     companion object {
         val INTERNVAL_TIME_MS = 16
     }

    var shouldUpdatePosition = false

    var vy: Float = 0f

    val VY_MULTIPLIER = 0.01f // px/ms
    val MIN_VY = 1
    val MAX_VY = 5

    fun updatePosition(timeMs: Int) {

        if (wallTop.isTouch(startPoint, 10F) || mBaseWallBottom!!.isTouch(startPoint, 10F)) {
            onCollision(CollisionListener.DIRECTION_VERTICAL)
        }

        startPoint.y += vy * timeMs

        EventBus.getDefault().post(this)
    }

    override fun onCollision(direction: Int) {
        if (direction == CollisionListener.DIRECTION_VERTICAL) {
            vy = -vy
        }
    }


    private fun randomWall(): Int {
        return 50 + (Math.random() * 50).toInt()
    }

    private fun randomVY(): Float {
        val random = Random()
        var vy = (MIN_VY + random.nextInt(MAX_VY)) * VY_MULTIPLIER
        vy = if (random.nextBoolean()) vy else -vy
        return vy
    }

    fun draw(canvas: ICanvasGL) {
        canvas.save()
        canvas.drawBitmap(bitmap, startPoint.x.toInt(), startPoint.y.toInt())

        //更新火箭位置
        if (shouldUpdatePosition)
            updatePosition(INTERNVAL_TIME_MS)
        canvas.restore()
    }

    override fun onTimeCountEnd() {
        val anim = ObjectAnimator.ofFloat(startPoint.y, targetPoint.y)
        anim.duration = (1000 + Math.random() * 1000).toLong()
        anim.interpolator = OvershootInterpolator()
        anim.addUpdateListener {
            startPoint.y = it.animatedValue.toString().toFloat()

            EventBus.getDefault().post(this)
        }
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {

                wallTop = BaseWall.BaseWallY(startPoint.y - randomWall())
                wallLeft = BaseWall.BaseWallX(startPoint.x)
                mBaseWallBottom = BaseWall.BaseWallY(startPoint.y + randomWall())
                mBaseWallRight = BaseWall.BaseWallX(startPoint.x)
                vy = randomVY()

                shouldUpdatePosition = true
            }
        })
        anim.start()
    }
}