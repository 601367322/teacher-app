package com.prance.teacher.features.redpackage.view.red

import android.content.Context
import android.graphics.*
import com.blankj.utilcode.util.Utils
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.students.model.StudentsEntity
import java.util.concurrent.CopyOnWriteArrayList


class RedPackageManager {

    companion object {

        //红包每个泳道最小的时间间隔
        const val minInterval = 2000

        //随机取值范围
        const val intervalRange = 4000

        //红包的动画总长
        const val translationDurationTime = 6000L

        const val alphaDurationTime = 200L

        const val DEFAULT_ALPHA = 255

        //红包的默认分数
        var DEFAULT_SCORE = 2

        var DEFAULT_WIDTH = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m300_0)
        var DEFAULT_HEIGHT = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m300_0)

        //总列数
        const val lines = 5

        var DEFAULT_SCALE = 1.5F
    }


    //屏幕宽高
    private var screenWidth: Int = Utils.getApp().resources.displayMetrics.widthPixels

    //红包集合
    private val redPackages = CopyOnWriteArrayList<RedPackage>()

    private val titles = mutableListOf("A", "B", "C", "D")


    //红包分数集合
    var studentScores = mutableListOf<StudentScore>()

    //红包背景
    private var redPackageImg: MutableMap<String, Bitmap>

    //红包积分数字图
    private var scoreBitmaps: MutableMap<String, Bitmap>

    //抢到小红包提示背景
    private var tipBitmapLittle: Bitmap

    //抢到大红包提示背景
    private var tipBitmapBig: Bitmap

    private var context: Context

    private var destroyRedPackageNum = 0

    constructor(context: Context) {

        this.context = context

        redPackageImg = mutableMapOf(
                "A" to createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.red_package_a), DEFAULT_WIDTH),
                "B" to createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.red_package_b), DEFAULT_WIDTH),
                "C" to createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.red_package_c), DEFAULT_WIDTH),
                "D" to createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.red_package_d), DEFAULT_WIDTH))

        val scoreMaxWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m48_0)

        scoreBitmaps = mutableMapOf(
                "+" to createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.red_package_score_add), scoreMaxWidth),
                "2" to createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.red_package_score_2), scoreMaxWidth),
                "4" to createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.red_package_score_4), scoreMaxWidth))

        val tipBitmap = createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.red_package_tip_background), Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m236_0))
        tipBitmapLittle = createTipBitmap(tipBitmap, "+2")
        tipBitmapBig = createTipBitmap(tipBitmap, "+4")

        //提前扩充内存，避免卡顿
        ScoreTip(context, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, StudentsEntity("test", ""), tipBitmapLittle, true)
    }

    fun generateRedPack(): RedPackage? {
        getAvailableLine()?.let {

            val randomTitle = titles[(Math.random() * (titles.size)).toInt()]

            var big = false

            //每10个红包，出现一个大红包  &  最后一个红包是小红包的前提下
            if (destroyRedPackageNum > 0 && destroyRedPackageNum % 4 == 0 && (redPackages.isNotEmpty() && !redPackages.last().big)) {
                big = true
            }

            var tip: Bitmap = if (big) tipBitmapBig else tipBitmapLittle

            val red = RedPackage(
                    context,
                    DEFAULT_WIDTH,
                    DEFAULT_HEIGHT,
                    randomTitle,
                    it,
                    big,
                    redPackageImg[randomTitle]!!,
                    tip
            )

            redPackages.add(red)

            destroyRedPackageNum++
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
                //如果红包的生命小于2秒，就是不可用的泳道
                if (!checkAvailableRedPackageLife(pack)) {
                    //去除不用的泳道
                    unAvailableLines.add(pack.lineNum)
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
     * 计算红包的生命时长是否大于2秒
     */
    private fun checkAvailableRedPackageLife(redPackage: RedPackage): Boolean {
        return (System.currentTimeMillis() - redPackage.createTime) > (minInterval + Math.random() * intervalRange)
    }

    private fun createBitmap(baseBitmap: Bitmap, width: Int): Bitmap {
        // 初始化Matrix对象
        val matrix = Matrix()
        // 根据传入的参数设置缩放比例
        matrix.postScale(width.toFloat() / baseBitmap.width.toFloat(), width.toFloat() / baseBitmap.width.toFloat())
        return Bitmap.createBitmap(baseBitmap, 0, 0, baseBitmap.width, baseBitmap.height, matrix, true)
    }

    private fun createTipBitmap(tipBitmap: Bitmap, score: String): Bitmap {
        //底部文字溢出长度
        val bottomPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m30_0)
        //右部文字溢出长度
        val rightPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m71_0)

        //红包背景378
        var bitmap = Bitmap.createBitmap(
                tipBitmap.width + rightPadding * 2,
                tipBitmap.height + bottomPadding,
                Bitmap.Config.ARGB_4444)

        val canvas = Canvas(bitmap)

        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(
                tipBitmap,
                (bitmap.width - tipBitmap.width).toFloat() / 2F,
                0F,
                null)

        val scores = convertTextToBitmap(score)
        var startX = bitmap.width - Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m86_0).toFloat()
        for (score in scores) {
            //积分图
            canvas.drawBitmap(score, startX, bitmap.height - Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m98_0).toFloat() - score.height, null)
            startX += Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m35_0)
        }
        return bitmap
    }

    private fun convertTextToBitmap(text: String): MutableList<Bitmap> {
        val list = mutableListOf<Bitmap>()
        for (i in text) {
            list.add(this.scoreBitmaps[i.toString()]!!)
        }
        return list
    }

    /**
     * 计算是否抢到了红包
     */
    @Synchronized
    fun grabRedPackage(keyID: String, sInfo: String?) {
        sInfo?.let {
            //找到最下面那个没有被抢到的红包
            var redPackage: RedPackage? = null
            for (i in redPackages) {
                if (i.state == RedPackageStatus.CANGRAB) {
                    redPackage = i
                    break
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
                    //计算红包被抢到的数量
                    destroyRedPackageNum++
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
            ClassesDetailFragment.mStudentList?.let {
                for (item in it) {
                    if (KeyID == item.getClicker()?.number) {
                        studentEntity = item
                    }
                }
            }
            //如果学生信息没有找到，则放弃处理
            if (studentEntity == null) {
                return null
            }

            //找到学生后，添加一个新的答题积分记录
            studentScore = StudentScore(studentEntity!!, 0, 0)
            studentScores.add(studentScore)
        }

        //添加答题次数和积分
        studentScore.redPackageNum += 1
        studentScore.score += DEFAULT_SCORE

        return studentScore
    }

    fun destroy() {
    }
}