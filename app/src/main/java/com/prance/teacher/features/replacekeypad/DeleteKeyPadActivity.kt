package com.prance.teacher.features.replacekeypad

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.replacekeypad.view.DeleteKeyPadFragment
import com.prance.teacher.features.replacekeypad.view.DeleteKeyPadFragment.Companion.KEYPAD_LIST
import java.io.Serializable


class DeleteKeyPadActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context, list: MutableList<KeyPadEntity>): Intent {
            val intent = Intent(context, DeleteKeyPadActivity::class.java)
            intent.putExtra(KEYPAD_LIST, SerializableList(list))
            return intent
        }
    }

    override fun fragment(): BaseFragment = DeleteKeyPadFragment.forData(intent?.getSerializableExtra(KEYPAD_LIST))

    class SerializableList<T> constructor(var list: MutableList<T>) : Serializable
}