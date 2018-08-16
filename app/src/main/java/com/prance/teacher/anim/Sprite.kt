package com.prance.teacher.anim

import android.graphics.PointF
import java.io.Serializable

class Sprite : Serializable {

    //坐标
    var point: PointF
    //透明度
    var alpha: Int = 255
    //横向运动速度
    var vx: Float = 0.toFloat()
    //纵向运动速度
    var vy: Float = 0.toFloat()
    var collisionRadius: Float = 0.toFloat()

    var vRotate: Float = 0.toFloat()
    var rotateDegree: Float = 0.toFloat()

    constructor(point: PointF, vx: Float, vy: Float, vRotate: Float, collisionRadius: Float) {
        this.point = point
        this.vx = vx
        this.vy = vy
        this.collisionRadius = collisionRadius
        this.vRotate = vRotate
    }

    fun reset(point: PointF, vx: Float, vy: Float, vRotate: Float, collisionRadius: Float) {
        this.point = point
        this.vx = vx
        this.vy = vy
        this.vRotate = vRotate
        this.collisionRadius = collisionRadius
    }

    fun updatePosition(timeMs: Int) {
        point.x += vx * timeMs
        point.y += vy * timeMs
        rotateDegree += vRotate * timeMs
    }

    interface CollisionListener {
        fun onCollision(direction: Int)

        companion object {
            val DIRECTION_HORIZONTAL = 0
            val DIRECTION_VERTICAL = 1
        }
    }

    override fun toString(): String {
        return "MovableObj{" +
                "point=" + point +
                ", vx=" + vx +
                ", vy=" + vy +
                ", collisionRadius=" + collisionRadius +
                ", vRotate=" + vRotate +
                ", rotateDegree=" + rotateDegree +
                '}'.toString()
    }
}