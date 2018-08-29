package com.prance.teacher.features.pk.rocket

import android.graphics.PointF

/**
 * @author bingbing
 */
abstract class BaseWall(protected var value: Float) {

    abstract fun isTouch(point: PointF, objRadius: Float): Boolean

    class BaseWallX(value: Float) : BaseWall(value) {

        override fun isTouch(point: PointF, objRadius: Float): Boolean {
            return Math.abs(point.x - this.value) <= objRadius
        }
    }

    class BaseWallY(value: Float) : BaseWall(value) {

        override fun isTouch(point: PointF, objRadius: Float): Boolean {
            return Math.abs(point.y - this.value) <= objRadius
        }
    }
}
