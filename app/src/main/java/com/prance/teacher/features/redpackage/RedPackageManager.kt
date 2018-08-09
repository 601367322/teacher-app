package com.prance.teacher.features.redpackage

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.prance.teacher.R.id.name
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.redpackage.model.RedPackageBean
import com.prance.teacher.features.redpackage.model.RedPackageRecord
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.view.RedPackageView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 *Created by rich on 2018/7/26
 */

class RedPackageManager {

    var mContext: Context?
    /**
     * 每个红包获得的积分
     */
    var mScore: Int = 2
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
    /**
     * 抢红包的结果
     */
    var resultMaps: HashMap<String,Int> = HashMap()
    var results: ArrayList<RedPackageRecord> = ArrayList()
    var mHandler: Handler = Handler(Looper.getMainLooper())
    constructor(context: Context?,score: Int) {
        mContext = context
        mScore = score
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
                    view.mStatus = RedPackageStatus.GRAB
                    RedPackageView.canUseSigns.add(view.getChoose())
                    val keyToName = keyToName(KeyID)
                    mHandler.post({
                        view.grab(keyToName)
                    })
                    saveResult(KeyID)
                }
            }
        }
    }

    /**
     * 通过keyid查找对应的学生名字
     */
    fun keyToName(KeyID: String): String{
        Log.e("rich","KeyID= "+ KeyID)
        var name= KeyID
        ClassesDetailFragment.mStudentList?.let {
            for(studentsEntity in it){
                studentsEntity.run {
                    var number:String = clickers?.get(0)?.number.toString()
                    val replaceFirst = number.replaceFirst("^0*", "")
                    if (name.equals(replaceFirst)) {
                        this.name?.let {
                            name = it
                        }
                    }
                }
            }
        }
        return "$name  +$mScore"
    }

    /**
     * 保存抢到红包的结果
     */
    fun saveResult(KeyID: String){
        val value = resultMaps[KeyID]
        if (value == null){
            resultMaps[KeyID] = mScore
        } else {
            resultMaps[KeyID] = value + mScore
        }
        var red = RedPackageRecord()
        red.clickerId = KeyID
        red.answerTime = Date().time
        for (StudentsEntity in ClassesDetailFragment.mStudentList!!){
            StudentsEntity.clickers?.let {
                if (KeyID == it[0].number){
                    red.studentId = StudentsEntity.id.toString()
                }
            }
        }
        results.add(red)
    }
}