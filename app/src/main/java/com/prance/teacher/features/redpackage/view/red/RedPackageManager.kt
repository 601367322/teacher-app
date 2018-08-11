package com.prance.teacher.features.redpackage.view.red

import android.graphics.*
import com.blankj.utilcode.util.Utils
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.redpackage.model.RedPackageRecord
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.students.model.StudentsEntity
import java.util.*

class RedPackageManager {

    companion object {

        //红包每个泳道最小的时间间隔
        const val minInterval = 2000

        //随机取值范围
        const val intervalRange = 3000

        //红包的动画总长
        const val translationDurationTime = 5000L

        const val alphaDurationTime = 200L

        const val DEFAULT_ALPHA = 255

        //红包的默认分数
        var DEFAULT_SCORE = 2
    }

    var DEFAULT_WIDTH = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m66_0)
    var DEFAULT_HEIGHT = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m120_0)


    private var mRedPackageBackgroundPaint: Paint? = null
    private var mRedPackageTextPaint: Paint? = null

    //屏幕宽高
    var screenWidth: Int = Utils.getApp().resources.displayMetrics.widthPixels

    //红包集合
    val redPackages = mutableListOf<RedPackage>()

    val titles = mutableListOf("A", "B", "C", "D")

    //总列数
    val lines = 6

    //提前绘制好的红包
    val map = HashMap<String, Bitmap>()

    //红包分数集合
    var studentScores = mutableListOf<StudentScore>()

    constructor() {
        mRedPackageBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRedPackageBackgroundPaint!!.color = Color.RED
        mRedPackageTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRedPackageTextPaint!!.color = Color.WHITE
        mRedPackageTextPaint!!.textSize = 80F
        mRedPackageTextPaint!!.textAlign = Paint.Align.CENTER
    }

    fun generateRedPack(): RedPackage? {
        getAvailableLine()?.let {

            val randomTitle = titles[(Math.random() * (titles.size)).toInt()]

            val red = RedPackage(
                    getRedPackageStartX(it).toInt(),
                    -DEFAULT_HEIGHT,
                    System.currentTimeMillis(),
                    DEFAULT_ALPHA,
                    DEFAULT_WIDTH.toLong(),
                    DEFAULT_HEIGHT.toLong(),
                    randomTitle,
                    createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, randomTitle)
            )

            redPackages.add(red)
            return red
        }
        return null
    }

    /**
     * 查看可以添加红包的泳道
     */
    private fun getAvailableLine(): Int? {

        //假设所有泳道均为可用
        val availableLines = mutableListOf<Int>()
        for (i in 0 until lines) {
            availableLines.add(i)
        }

        val unAvailableLines = mutableListOf<Int>()

        for (pack in redPackages) {
            //如果是未销毁的红包
            if (pack.state != RedPackageStatus.FREE) {
                //如果红包的生命不大于2秒，就是不可用的泳道
                if (!checkAvailableRedPackageLife(pack)) {
                    //去除不用的泳道
                    val l = getLineNumberByStartX(pack.x)
                    unAvailableLines.add(l)

                }
            }
        }
        availableLines.removeAll(unAvailableLines)

        if (availableLines.isNotEmpty()) {
            //随机选出可用的泳道
            return availableLines[(Math.random() * (availableLines.size)).toInt()]
        }

        return null
    }

    /**
     * 根据泳道下标，获取起始X
     */
    private fun getStartXByLineNumber(lineNumber: Int): Float {
        return screenWidth.toFloat() / lines.toFloat() * lineNumber.toFloat()
    }

    /**
     * 根据泳道下标，获取起始X
     */
    private fun getLineNumberByStartX(x: Int): Int {
        val lineWidth = screenWidth.toFloat() / lines.toFloat()
        return (x / lineWidth).toInt()
    }

    /**
     * 泳道的宽度
     */
    private fun getLineWidth(): Float {
        return screenWidth.toFloat() / lines.toFloat()
    }

    /**
     * 红包开始的X
     */
    private fun getRedPackageStartX(lintNumber: Int): Float {
        val startX = getLineWidth() * lintNumber
        return ((getLineWidth() - DEFAULT_WIDTH) / 2) + startX
    }

    /**
     * 计算红包的生命时长是否大于2秒
     */
    private fun checkAvailableRedPackageLife(redPackage: RedPackage): Boolean {
        return (System.currentTimeMillis() - redPackage.createTime) > (minInterval + Math.random() * intervalRange)
    }

    private fun createBitmap(width: Int, height: Int, title: String): Bitmap {
        if (map.containsKey(title)) {
            return map[title]!!
        } else {
            //红包背景
            var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val targetRect = RectF(0F, 0F, width.toFloat(), height.toFloat())
            canvas.drawRect(targetRect, mRedPackageBackgroundPaint)

            //红包的答案
            val fontMetrics = mRedPackageTextPaint!!.fontMetricsInt
            val baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2
            canvas.drawText(title, targetRect.centerX(), baseline, mRedPackageTextPaint)
            canvas.save()
            map[title] = bitmap
            return bitmap
        }
    }

    /**
     * 计算是否抢到了红包
     */
    @Synchronized
    fun grabRedPackage(keyID: String, sInfo: String?) {
        sInfo?.let {
            //找到最下面那个没有被抢到的红包
            var redPackage: RedPackage? = null
            for (i in redPackages.indices.reversed()) {
                if (redPackages[i].state == RedPackageStatus.CANGRAB) {
                    redPackage = redPackages[i]
                }
            }
            redPackage?.let {
                if (sInfo == redPackage.title) {
                    //抢到了
                    //添加记录
                    val studentScore = saveResult(keyID)
                    studentScore?.let {
                        //销毁红包
                        redPackage.destroy(it)
                    }
                }
            }
        }
    }

    /**
     * 保存答题信息
     */
    private fun saveResult(KeyID: String): StudentScore? {

        //先找出已经存在缓存中的学生答题积分记录
        var studentScore: StudentScore? = null
        for (item in studentScores) {
            if (item.student.getClicker()?.number == KeyID) {
                studentScore = item
            }
        }
        //如果没有找到，则说明第一次答对题
        if (studentScore == null) {
            //根据keyId去找到对应的学生信息
            var studentEntity: StudentsEntity? = null
            for (item in ClassesDetailFragment.mStudentList!!) {
                if (KeyID == item.getClicker()?.number) {
                    studentEntity = item
                }
            }
            //如果学生信息没有找到，则放弃处理
            if (studentEntity == null) {
                return null
            }

            //找到学生后，添加一个新的答题积分记录
            studentScore = StudentScore(studentEntity, 0, 0)
            studentScores.add(studentScore)
        }

        //添加答题次数和积分
        studentScore.redPackageNum += 1
        studentScore.score += DEFAULT_SCORE

        return studentScore
    }
}