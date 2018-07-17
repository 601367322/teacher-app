package com.prance.lib.database


class KeyPadDaoUtils {

    private val mDao: KeyPadEntityDao = DaoManager.getDaoSession().keyPadEntityDao

    fun getAllKeyPadByBaseStationSN(serialNumber: String): MutableList<KeyPadEntity>? {
        return mDao.queryBuilder()
                .where(KeyPadEntityDao.Properties.BaseStationSN.eq(serialNumber))
                .list()
    }
}