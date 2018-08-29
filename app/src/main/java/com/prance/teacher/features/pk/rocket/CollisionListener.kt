package com.prance.teacher.features.pk.rocket

interface CollisionListener {
    fun onCollision(direction: Int)

    companion object {
        val DIRECTION_HORIZONTAL = 0
        val DIRECTION_VERTICAL = 1
    }
}
