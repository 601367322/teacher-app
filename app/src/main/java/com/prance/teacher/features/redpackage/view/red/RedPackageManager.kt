package com.prance.teacher.features.redpackage.view.red

import android.content.Context
import android.graphics.*
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.BuildConfig
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
        const val fastTranslationDurationTime = 4500L

        const val alphaDurationTime = 200L

        const val DEFAULT_ALPHA = 255

        //红包的默认分数
        var DEFAULT_SCORE = 2

        var DEFAULT_WIDTH = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m300_0)
        var DEFAULT_HEIGHT = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m300_0)

        //总列数
        const val lines = 5

        var DEFAULT_SCALE = 1.3F
    }


    //屏幕宽高
    private var screenWidth: Int = Utils.getApp().resources.displayMetrics.widthPixels

    //红包集合
    private val redPackages = CopyOnWriteArrayList<RedPackage>()

    private val titles = mutableListOf("A", "B", "C", "D")


    //红包分数集合
    var studentScores = mutableListOf<StudentScore>()

    //红包背景
    var redPackageImg: MutableList<Bitmap> = mutableListOf()

    //红包背景
    var bigRedPackageImg: MutableList<Bitmap> = mutableListOf()

    //红包ABCD
    var redPackageTitle: MutableMap<String, Bitmap> = mutableMapOf()

    //红包积分数字图
    var scoreBitmaps: MutableMap<String, Bitmap> = mutableMapOf()

    //气泡
    var bubble: Bitmap
    var bubble1: Bitmap
    var bubble2: Bitmap

    //抢到红包提示背景
    var tipBitmap: Bitmap

    private var context: Context

    private var destroyRedPackageNum = 0

    private var nextIsBig = false

    //间隔大红包数量
    private var destroyIntervalNum = 10

    constructor(context: Context) {

        this.context = context

        //红包Gif资源
        val redPackageRes = mutableListOf(
                R.drawable.red_package_00000,
                R.drawable.red_package_00001,
                R.drawable.red_package_00002,
                R.drawable.red_package_00003,
                R.drawable.red_package_00004,
                R.drawable.red_package_00005,
                R.drawable.red_package_00006,
                R.drawable.red_package_00007,
                R.drawable.red_package_00008,
                R.drawable.red_package_00009,
                R.drawable.red_package_00010,
                R.drawable.red_package_00011,
                R.drawable.red_package_00012,
                R.drawable.red_package_00013,
                R.drawable.red_package_00014,
                R.drawable.red_package_00015
        )

        for (i in redPackageRes) {
            redPackageImg.add(
                    GlideApp.with(context)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .load(i)
                            .submit(DEFAULT_WIDTH, DEFAULT_WIDTH)
                            .get()
            )
        }

        //红包Gif资源
        val bigRedPackageRes = mutableListOf(
                R.drawable.big_red_package_00000,
                R.drawable.big_red_package_00001,
                R.drawable.big_red_package_00002,
                R.drawable.big_red_package_00003,
                R.drawable.big_red_package_00004,
                R.drawable.big_red_package_00005,
                R.drawable.big_red_package_00006,
                R.drawable.big_red_package_00007
        )

        for (i in bigRedPackageRes) {
            bigRedPackageImg.add(
                    GlideApp.with(context)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .load(i)
                            .submit(DEFAULT_WIDTH, DEFAULT_WIDTH)
                            .get()
            )
        }

        //红包标题 ABCD
        val redPackageTitleRes = mutableMapOf(
                "A" to R.drawable.red_package_title_a,
                "B" to R.drawable.red_package_title_b,
                "C" to R.drawable.red_package_title_c,
                "D" to R.drawable.red_package_title_d
        )

        val titleWidth = context.resources.getDimensionPixelOffset(R.dimen.m50_0)

        for (i in redPackageTitleRes) {
            redPackageTitle[i.key] = GlideApp.with(context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(i.value)
                    .submit(titleWidth, titleWidth)
                    .get()
        }

        //气泡
        bubble = GlideApp.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(R.drawable.red_package_pao)
                .submit(DEFAULT_WIDTH, DEFAULT_WIDTH)
                .get()
        //气泡
        bubble1 = GlideApp.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(R.drawable.big_red_package_pao1)
                .submit(DEFAULT_WIDTH, DEFAULT_WIDTH)
                .get()
        //气泡
        bubble2 = GlideApp.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(R.drawable.big_red_package_pao2)
                .submit(DEFAULT_WIDTH, DEFAULT_WIDTH)
                .get()

        //积分资源
        val scoreMaxWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m48_0)
        val scoreRes = mutableMapOf(
                "+" to R.drawable.red_package_score_add,
                "0" to R.drawable.red_package_score_0,
                "1" to R.drawable.red_package_score_1,
                "2" to R.drawable.red_package_score_2,
                "3" to R.drawable.red_package_score_3,
                "4" to R.drawable.red_package_score_4,
                "5" to R.drawable.red_package_score_5,
                "6" to R.drawable.red_package_score_6,
                "7" to R.drawable.red_package_score_7,
                "8" to R.drawable.red_package_score_8,
                "9" to R.drawable.red_package_score_9
        )
        for (i in scoreRes) {
            scoreBitmaps[i.key] = GlideApp.with(context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(i.value)
                    .submit(scoreMaxWidth, scoreMaxWidth)
                    .get()
        }

        //抢到红包提示
        tipBitmap = GlideApp.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(R.drawable.red_package_tip_background)
                .submit(context.resources.getDimensionPixelOffset(R.dimen.m210_0), context.resources.getDimensionPixelOffset(R.dimen.m214_0))
                .get()
    }

    fun generateRedPack(fast: Boolean = false): RedPackage? {
        getAvailableLine()?.let {

            val randomTitle = titles[(Math.random() * (titles.size)).toInt()]

            var big = false

            if (BuildConfig.DEBUG) {
//                destroyIntervalNum = 4
            }

            //每10个红包，出现一个大红包  &  最后一个红包是小红包的前提下
            if (nextIsBig) {
                big = true
                nextIsBig = false
            }

            val score = if (big) DEFAULT_SCORE * 2 else DEFAULT_SCORE

            val red = RedPackage(
                    context,
                    DEFAULT_WIDTH,
                    DEFAULT_HEIGHT,
                    randomTitle,
                    it,
                    big,
                    score,
                    if (big) Bubble(mutableListOf(bubble1, bubble2)) else Bubble(mutableListOf(bubble)),
                    redPackageTitle[randomTitle]!!,
                    if (big) bigRedPackageImg else redPackageImg,
                    tipBitmap,
                    scoreBitmaps,
                    fast
            )

            redPackages.add(red)

            if (BuildConfig.DEBUG) {
//                destroyRedPackageNum++
            }
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
                    //计算红包被抢到的数量
                    destroyRedPackageNum++
                    if (destroyRedPackageNum % destroyIntervalNum == 0) {
                        nextIsBig = true
                    }
                    val studentScore = saveResult(keyID, redPackage.score)
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
    private fun saveResult(KeyID: String, score: Int): StudentScore? {

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

            if (BuildConfig.DEBUG) {
//                studentEntity = StudentsEntity("", "")
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
        studentScore.score += score

        return studentScore
    }

    fun destroy() {
        for(i in redPackages){
            i.destroy()
        }
        redPackages.clear()
    }
}