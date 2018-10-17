package com.prance.teacher.features.modifybind.view

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.teacher.features.modifybind.contract.IChooseKeyPadContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.deletekeypad.DeleteKeyPadActivity
import com.prance.teacher.features.match.view.MatchedKeyPadAdapter
import com.prance.teacher.features.modifybind.ChooseKeyPadActivity
import com.prance.teacher.features.modifybind.presenter.ChooseKeyPadPresenter
import com.prance.teacher.features.students.model.StudentsEntity
import com.spark.teaching.answertool.usb.model.ReportBindCard
import kotlinx.android.synthetic.main.fragment_choose_keypad.*

/**
 * 配对答题器
 */
class ChooseKeyPadFragment : BaseFragment(), IChooseKeyPadContract.View {

    override var mPresenter: IChooseKeyPadContract.Presenter = ChooseKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_choose_keypad

    private var mAdapter = MatchedKeyPadAdapter(R.layout.item_delete_key_pad)

    lateinit var mEmptyView: ImageView

    private lateinit var mStudents: MutableList<StudentsEntity>

    private var mOldKeyPadList: MutableList<KeyPadEntity>? = null

    lateinit var mClassesEntity: ClassesEntity
    var mPosition: Int? = 0

    companion object {

        fun forStudents(list: DeleteKeyPadActivity.SerializableList<StudentsEntity>, classes: ClassesEntity, position: Int): ChooseKeyPadFragment {
            val fragment = ChooseKeyPadFragment()
            val arguments = Bundle()
            arguments.putSerializable(StudentsModifyBindFragment.STUDENTS, list)
            arguments.putSerializable(ChooseKeyPadActivity.CLASSES, classes)
            arguments.putSerializable(ChooseKeyPadActivity.POSITION, position)
            fragment.arguments = arguments
            return fragment
        }
    }

    private val mSparkServicePresenter: SparkServicePresenter  by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onCardBind(reportBindCard: ReportBindCard) {


                val keyId = reportBindCard.uid.toString()
                if (!isExists(keyId)) {

                    if (isExistsInStudents(keyId)) {
                        ToastUtils.showShort("该答题器已绑定其他学员")
                        return
                    }

                    val keyPadEntity = KeyPadEntity(SparkService.mUsbSerialNum, keyId)

                    mAdapter.addData(keyPadEntity)
                    mAdapter.notifyDataSetChanged()

                    recycler.postDelayed({
                        //最后一个答题器获取焦点
                        recycler.layoutManager.findViewByPosition(mAdapter.data.size - 1).requestFocus()
                    }, 250)

                    displayMoreBtn()
                }
            }

            override fun onServiceConnected() {
                //查找已经配对过的答题器
                SparkService.mUsbSerialNum?.run {
                    mPresenter.getMatchedKeyPadByBaseStationId(this)
                }
            }

        })
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        mStudents = (arguments?.getSerializable(StudentsModifyBindFragment.STUDENTS) as DeleteKeyPadActivity.SerializableList<StudentsEntity>).list

        mClassesEntity = arguments?.getSerializable(ChooseKeyPadActivity.CLASSES) as ClassesEntity

        mPosition = arguments?.getInt(ChooseKeyPadActivity.POSITION)

        mEmptyView = rootView.findViewById(R.id.emptyImage)

        //设置显示格式
        recycler.layoutManager = FocusGridLayoutManager(context!!, 6)

        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = resources.getDimensionPixelOffset(R.dimen.m40_0)
            }
        })

        mAdapter.setOnItemChildClickListener { _, _, position ->
            val keyPadEntity = mAdapter.getItem(position)

            context?.run {
                AlertDialog(this)
                        .setMessage("确认使用该答题器吗？")
                        .setCancelButton("取消", null)
                        .setConfirmButton("确认") { _ ->
                            showProgress()
                            mPresenter.modifyBind(mClassesEntity.klass!!.id.toString(), mStudents[position], keyPadEntity!!.keyId)
                        }
                        .show()
            }
        }

        //添加数据源
        recycler.adapter = mAdapter

        //按钮页面状态初始化
        displayMoreBtn()

        mSparkServicePresenter.bind()
    }

    /**
     * 渲染答题器
     *
     * list 绑定过的答题器列表
     */
    override fun renderKeyPadItemFromDatabase(list: MutableList<KeyPadEntity>) {
        mOldKeyPadList = list
        val newList = mutableListOf<KeyPadEntity>()
        mStudents.run {
            for (k in list) {
                val exist = isExistsInStudents(k.keyId)
                if (!exist) {
                    newList.add(k)
                }
            }
        }

        mAdapter.setNewData(newList)
        mAdapter.notifyDataSetChanged()

        displayMoreBtn()
    }

    override fun modifySuccess() {
        ToastUtils.showShort("绑定成功")
        activity?.run {
            val intent = Intent()
            intent.putExtra(StudentsModifyBindFragment.STUDENTS, DeleteKeyPadActivity.SerializableList(mStudents))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    /**
     * 检查去重配对
     */
    private fun isExists(KeyID: String?): Boolean {
        for (keyPad in mAdapter.data) {
            if (keyPad.keyId == KeyID) {
                return true
            }
        }
        return false
    }

    private fun isExistsInStudents(KeyID: String?): Boolean {
        mStudents.run {
            for (s in this) {
                if (KeyID == s.getClicker()?.number) {
                    return true
                }
            }
        }
        return false
    }

    private fun displayMoreBtn() {
        displayEmptyView()
    }

    /**
     * 空状态展示
     */
    private fun displayEmptyView() {

        if (mAdapter.data.isEmpty()) {
            emptyImage.visible()
            recycler.invisible()

            val animationDrawable = emptyImage.drawable as AnimationDrawable
            animationDrawable.start()
        } else {
            emptyImage.invisible()
            recycler.visible()
        }
    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        ToastUtils.showShort("绑定失败，请换个答题器")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        mSparkServicePresenter.unBind()

        val animationDrawable = mEmptyView.drawable as AnimationDrawable
        animationDrawable.stop()
    }
}