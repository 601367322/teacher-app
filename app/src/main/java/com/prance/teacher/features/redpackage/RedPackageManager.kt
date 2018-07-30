package com.prance.teacher.features.redpackage

import android.content.Context
import android.text.TextUtils
import com.prance.teacher.features.redpackage.model.RedPackageBean
import com.prance.teacher.features.redpackage.model.RedPackageWrapper
import com.prance.teacher.features.redpackage.view.RedPackageView
import java.util.*
import kotlin.collections.ArrayList

/**
 *Created by rich on 2018/7/26
 */

class RedPackageManager {

    var mContext: Context?
    /**
     * 存放红包
     */
    var mRedPackage: LinkedList<RedPackageWrapper> = LinkedList()
    /**
     * 泳道数量
     */
    val roadCount: Int = 6
    /**
     * 各个泳道上一个红包的显示时间
     */
    var showTimes: Array<Long> = Array(roadCount, { 0L })
    /**
     * 可以被使用的选项
     */
    var canUseSigns: LinkedList<String> = LinkedList()

    constructor(context: Context?) {
        mContext = context
        canUseSigns.addAll(arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"))
    }

    /**
     * 试图从缓存池中获取红包
     */
    fun obtainPackage(): RedPackageWrapper? {
        val randomParams = randomParams();
        if (randomParams == null) {
            return null
        }
        var redPackageWrapper: RedPackageWrapper
        if (mRedPackage.size == 0) {
            redPackageWrapper = createRedPackage(null, randomParams)
        } else {
            var redPackage = mRedPackage.get(0)
            //是否已经被recycle过
            if (redPackage.view.parent == null) {
                redPackageWrapper = createRedPackage(redPackage.view, randomParams)
            } else {
                redPackageWrapper = createRedPackage(null, randomParams)
            }
        }
        //记录各个泳道上一个红包的显示时间
        showTimes[redPackageWrapper.model.roadPosition] = Date().time
        return redPackageWrapper
    }

    /**
     * 创建一个红包
     * @param recycleView 若等于null复用已被回收的view
     */
    fun createRedPackage(recycleView: RedPackageView?, randomParams: RedPackageBean): RedPackageWrapper {
        val redPackageWrapper = RedPackageWrapper()

        var redPackageView = recycleView
        if (redPackageView == null) {
            redPackageView = RedPackageView(mContext);
        }
        redPackageView.setChoose(randomParams.chooseSign)

        redPackageWrapper.view = redPackageView
        redPackageWrapper.model = randomParams
        mRedPackage.addLast(redPackageWrapper)
        return redPackageWrapper
    }

    fun recycleRedPackage(redPackageView: RedPackageView) {
    }

    /**
     * 随机生成红包的一些参数，返回null说明按照规则已经无法生成红包
     */
    fun randomParams(): RedPackageBean? {
        val redPackageBean = RedPackageBean()
        val random = Random()
        if (canUseSigns.size > 0) {
            val nextInt = random.nextInt(canUseSigns.size);
            redPackageBean.chooseSign = canUseSigns[nextInt]
            canUseSigns.removeAt(nextInt)
        } else {
            return null
        }
        //随机选取泳道
        val nowTime = Date().time
        val canUseRoads = ArrayList<Int>(roadCount)
        for ((index, value) in showTimes.withIndex()) {
            if (nowTime - value > 2000) {
                canUseRoads.add(index)
            }
        }
        if (canUseRoads.size > 0) {
            redPackageBean.roadPosition = canUseRoads[random.nextInt(canUseRoads.size)]
        } else {
            return null
        }
        return redPackageBean
    }

    fun grabRedPackage(KeyID: String, sInfo: String?){
        if (!TextUtils.isEmpty(sInfo)){

        }
    }
}