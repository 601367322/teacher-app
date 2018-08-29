package com.prance.teacher.features.pk.rocket

import android.graphics.Bitmap
import android.graphics.PointF
import com.chillingvan.canvasgl.ICanvasGL
import java.util.*

abstract class BaseRocket(var point: PointF, var collisionRadius: Float) : CollisionListener {

    var wallTop = BaseWall.BaseWallY(0F)
    var wallLeft = BaseWall.BaseWallX(0F)
    var mBaseWallBottom: BaseWall? = null
    var mBaseWallRight: BaseWall? = null

    private val INTERNVAL_TIME_MS = 16

    var vy: Float
    var vx: Float

    val VY_MULTIPLIER = 0.01f // px/ms
    val VX_MULTIPLIER = 0.01f
    val MIN_VY = 1
    val MAX_VY = 3
    val MIN_VX = 1
    val MAX_VX = 3

    abstract var bitmap: Bitmap

    fun updatePosition(timeMs: Int) {
        
        if (wallTop.isTouch(point, collisionRadius) || mBaseWallBottom!!.isTouch(point, collisionRadius)) {
            onCollision(CollisionListener.DIRECTION_VERTICAL)
        } else if (wallLeft.isTouch(point, collisionRadius) || mBaseWallRight!!.isTouch(point, collisionRadius)) {
            onCollision(CollisionListener.DIRECTION_HORIZONTAL)
        }
        
        point.x += vx * timeMs
        point.y += vy * timeMs
    }

    override fun onCollision(direction: Int) {
        if (direction == CollisionListener.DIRECTION_HORIZONTAL) {
            vx = -vx
        } else if (direction == CollisionListener.DIRECTION_VERTICAL) {
            vy = -vy
        }
    }

    init {
        wallTop = BaseWall.BaseWallY(point.y - 200)
        wallLeft = BaseWall.BaseWallX(point.x - 200)
        mBaseWallBottom = BaseWall.BaseWallY(point.y + 200)
        mBaseWallRight = BaseWall.BaseWallX(point.x + 200)
        vx = randomVX()
        vy = randomVY()
    }


    private fun randomVY(): Float {
        val random = Random()
        var vy = (MIN_VY + random.nextInt(MAX_VY)) * VY_MULTIPLIER
        vy = if (random.nextBoolean()) vy else -vy
        return vy
    }

    private fun randomVX(): Float {
        val random = Random()
        var vx = (MIN_VX + random.nextInt(MAX_VX)) * VX_MULTIPLIER
        vx = if (random.nextBoolean()) vx else -vx
        return vx
    }

    fun draw(canvas: ICanvasGL){
        canvas.save()
        canvas.drawBitmap(bitmap, point.x.toInt(), point.y.toInt())

        //更新火箭位置
        updatePosition(INTERNVAL_TIME_MS)
        canvas.restore()
    }

}