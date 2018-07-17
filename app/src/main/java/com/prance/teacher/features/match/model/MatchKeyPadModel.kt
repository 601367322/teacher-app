package com.prance.teacher.features.match.model

import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.database.KeyPadDaoUtils
import com.prance.lib.database.KeyPadEntity

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午3:32
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class MatchKeyPadModel : BaseModelKt(), IMatchKeyPadContract.Model {

    private var mKeyPadDaoUtils: KeyPadDaoUtils = KeyPadDaoUtils()

    override fun saveAllMatchedKeyPad(data: List<KeyPadEntity>): Boolean {
        return mKeyPadDaoUtils.saveKeyPad(data)
    }

    override fun deleteKeyPad(serialNumber: String): Boolean {
        return mKeyPadDaoUtils.deleteKeyPad(serialNumber)
    }
}

