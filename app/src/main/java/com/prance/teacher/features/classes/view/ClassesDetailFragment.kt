package com.prance.teacher.features.classes.view

import android.app.Service
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import cn.sunars.sdk.SunARS
import cn.sunars.sdk.SunARS.removeListener
import com.blankj.utilcode.util.ActivityUtils
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.socket.PushService.Companion.ATTEND_CLASS
import com.prance.lib.socket.PushService.Companion.CMD_SEND_QUESTION
import com.prance.lib.socket.model.MessageEntity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.students.view.StudentsFragment.Companion.CLASSES
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.utils.IntentUtils
import kotlinx.android.synthetic.main.fragment_classes_detail.*
import org.json.JSONObject
import java.io.Serializable

class ClassesDetailFragment : BaseFragment(), MessageListener {

    override fun layoutId(): Int = R.layout.fragment_classes_detail

    lateinit var mClassesEntity: ClassesEntity

    var mPushBinder: PushService.PushServiceBinder? = null

    private var mPushServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPushBinder = service as PushService.PushServiceBinder
            mPushBinder?.addListener(this@ClassesDetailFragment)
        }
    }

    companion object {

        fun forClasses(classes: ClassesEntity): ClassesDetailFragment {
            val fragment = ClassesDetailFragment()
            val arguments = Bundle()
            arguments.putSerializable(CLASSES, classes)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mClassesEntity = arguments?.getSerializable(CLASSES) as ClassesEntity

        readyClass.setOnClickListener {
            context?.let {
                AlertDialog(it)
                        .setMessage("确定准备就绪？")
                        .setConfirmButton("确定", {
                            try {
                                startActivity(IntentUtils.callingXYDial())
                            } catch (e: Exception) {
                                e.printStackTrace()
                                ToastUtils.showShort("请使用小鱼易联")
                            }
                        })
                        .setCancelButton("取消", null)
                        .show()
            }
        }

        endClass.setOnClickListener { activity?.finish() }

        classesTitle.text = mClassesEntity.klass?.name
        classesSubTitle.text = mClassesEntity.klass?.addr
        classesDate.text = """${mClassesEntity.klass?.startTime}-${mClassesEntity.klass?.endTime}"""

        //开始Socket监听
        activity?.bindService(PushService.callingIntent(context!!), mPushServiceConnection, Service.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()

        mPushBinder?.run {
            removeListener(this@ClassesDetailFragment)
        }
        //关闭Socket监听
        activity?.unbindService(mPushServiceConnection)
    }


    override fun onMessageResponse(msg: MessageEntity) {
        if (msg.cmd == CMD_SEND_QUESTION) {
            //开始答题
            val question = msg.create(Question::class.java)
            if (question.classId == mClassesEntity.klass?.id) {
                ActivityUtils.finishActivity(SubjectActivity::class.java)
                context?.run {
                    startActivity(SubjectActivity.callingIntent(this, question))
                }
            }
        }

    }

    override fun needSunVoteService(): Boolean = true

    class Question : Serializable {

        var classId: Int? = null
        var type: Int? = null
        var param: String? = null
        var questionId: Int? = null
    }

    override fun onServiceStatusConnectChanged(statusCode: Int) {
        when (statusCode) {
            MessageListener.STATUS_CONNECT_SUCCESS -> {
                val json = JSONObject()
                json.put(PushService.CMD, ATTEND_CLASS)
                json.put("classId", mClassesEntity.klass?.id)
                mPushBinder?.sendMessage(json.toString())
            }
        }
    }
}