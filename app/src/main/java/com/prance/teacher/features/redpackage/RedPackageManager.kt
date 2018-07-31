package com.prance.teacher.features.redpackage

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.prance.teacher.features.redpackage.model.RedPackageBean
import com.prance.teacher.features.redpackage.model.RedPackageStatus
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
    var mRedPackage: LinkedList<RedPackageView> = LinkedList()
    /**
     * 泳道数量
     */
    val roadCount: Int = 6
    /**
     * 各个泳道上一个红包的显示时间
     */
    var showTimes: Array<Long> = Array(roadCount, { 0L })

    constructor(context: Context?) {
        mContext = context
    }

    /**
     * 试图从缓存池中获取红包
     */
    fun obtainPackage(): RedPackageView? {
        val randomParams = randomParams();
        if (randomParams == null) {
            return null
        }
        var redPackageView: RedPackageView
        if (mRedPackage.size == 0) {
            redPackageView = createRedPackage(null, randomParams)
            mRedPackage.addLast(redPackageView)
        } else {
            var redPackage = findRecycleView()
            if (redPackage != null){
                redPackageView = createRedPackage(redPackage, randomParams)
            } else {
                redPackageView = createRedPackage(null, randomParams)
                mRedPackage.addLast(redPackageView)
            }
        }
        //记录各个泳道上一个红包的显示时间
        showTimes[randomParams.roadPosition] = Date().time
        return redPackageView
    }

    /**
     * 寻找可以利用的红包view
     */
    fun findRecycleView(): RedPackageView?{
        var redPackageView: RedPackageView? = null
        for (view in mRedPackage){
            if (view.parent == null){
                redPackageView = view
                break
            }
        }
        return redPackageView
    }
    /**
     * 创建一个红包
     * @param recycleView 若等于null复用已被回收的view
     */
    fun createRedPackage(recycleView: RedPackageView?, randomParams: RedPackageBean): RedPackageView {
        var redPackageView = recycleView
        if (redPackageView == null) {
            redPackageView = RedPackageView(mContext);
        }

        redPackageView.setChoose(randomParams.chooseSign)
        redPackageView.roadPosition = randomParams.roadPosition
        return redPackageView
    }

    /**
     * 随机生成红包的一些参数，返回null说明按照规则已经无法生成红包
     */
    fun randomParams(): RedPackageBean? {
        val redPackageBean = RedPackageBean()
        val random = Random()
        if (RedPackageView.canUseSigns.size > 0) {
            val nextInt = random.nextInt(RedPackageView.canUseSigns.size);
            redPackageBean.chooseSign = RedPackageView.canUseSigns[nextInt]
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

    /**
     * 计算是否抢到了红包
     */
    fun grabRedPackage(KeyID: String, sInfo: String?){
        if (!TextUtils.isEmpty(sInfo)){
            for (view in mRedPackage) {
                if (view.mStatus == RedPackageStatus.CANGRAB && view.getChoose().equals(sInfo)) {
                    view.grab(keyToName(KeyID))
                    saveResult()
                }
            }
        }
    }

    /**
     * 通过keid查找对应的学生名字
     */
    fun keyToName(KeyID: String): String{
        return KeyID + "抢到了红包"
    }

    /**
     * 保存抢到红包的结果
     */
    fun saveResult(){

    }
}