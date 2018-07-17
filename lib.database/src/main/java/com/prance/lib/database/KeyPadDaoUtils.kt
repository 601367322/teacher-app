package com.prance.lib.database


class KeyPadDaoUtils {

    private val mDao: KeyPadEntityDao = DaoManager.getDaoSession().keyPadEntityDao

    fun getAllKeyPadByBaseStationSN(serialNumber: String): MutableList<KeyPadEntity>? {
        return mDao.queryBuilder()
                .where(KeyPadEntityDao.Properties.BaseStationSN.eq(serialNumber))
                .list()
    }

    fun saveKeyPad(data: List<KeyPadEntity>): Boolean {
        try {
            for (keyPad in data) {
                mDao.insert(keyPad)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun deleteKeyPad(serialNumber: String):Boolean {
        try {
            val builder = mDao.queryBuilder().where(KeyPadEntityDao.Properties.BaseStationSN.eq(serialNumber)).buildDelete()
            builder.executeDeleteWithoutDetachingEntities()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }
}