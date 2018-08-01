package com.prance.teacher.features.redpackage.model

/**
 *Created by rich on 2018/7/30
 */

enum class RedPackageStatus {
    /**
     * 已被释放
     */
    FREE,
    /**
     * 已显示但不可抢
     */
    CANNOTGRAB,
    /**
     * 红包处于可抢状态
     */
    CANGRAB,
    /**
     * 红包已被抢
     */
    GRAB,
}