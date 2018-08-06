package com.prance.lib.database

import android.provider.SyncStateContract.Helpers.insert


class KeyPadDaoUtils {

    private val mDao: KeyPadEntityDao = DaoManager.daoSession?.keyPadEntityDao!!

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

    fun saveKeyPad(keyPadEntity: KeyPadEntity): KeyPadEntity? {
        try {
            keyPadEntity.id = mDao.insert(keyPadEntity)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return keyPadEntity
    }

    fun deleteKeyPad(serialNumber: String): Boolean {
        try {
            val builder = mDao.queryBuilder().where(KeyPadEntityDao.Properties.BaseStationSN.eq(serialNumber)).buildDelete()
            builder.executeDeleteWithoutDetachingEntities()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun deleteKeyPad(keyPadEntity: KeyPadEntity): Boolean {
        try {
            mDao.delete(keyPadEntity)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }
}