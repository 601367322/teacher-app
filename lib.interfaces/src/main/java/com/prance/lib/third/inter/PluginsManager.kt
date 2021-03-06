package com.prance.lib.third.inter

/**
 * 第三方功能模块解耦
 * Created by shenbingbing on 16/5/31.
 */
class PluginsManager {

    internal var mTestSetting: ITestSetting? = null
    internal var mBugly: IBugly? = null
    internal var mTeacher: ITeacher? = null
    internal var mSpark: ISpark? = null

    init {
        try {
            mTestSetting = getImpl<ITestSetting>("com.prance.third.impl.TestSettingImpl")
            mBugly = getImpl<IBugly>("com.prance.third.impl.BuglyImpl")
            mTeacher = getImpl<ITeacher>("com.prance.third.impl.TeacherImpl")
            mSpark = getImpl<ISpark>("com.prance.third.impl.SparkImpl")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun <T> getImpl(className: String): T? {
        try {
            val clazz = Class.forName(className)
            return clazz.newInstance() as T
        } catch (e: Exception) {
            //            e.printStackTrace();
        }
        return null
    }

    companion object {

        private var mInstance: PluginsManager? = null


        val instance: PluginsManager
            get() {
                if (mInstance == null) {
                    synchronized(PluginsManager::class.java) {
                        if (mInstance == null) {
                            mInstance = PluginsManager()
                        }
                    }
                }
                return mInstance!!
            }

        val testSetting: ITestSetting?
            get() = instance.mTestSetting

        val bugly: IBugly?
            get() = instance.mBugly

        val teacher: ITeacher?
            get() = instance.mTeacher

        val spark: ISpark?
            get() = instance.mSpark
    }
}
