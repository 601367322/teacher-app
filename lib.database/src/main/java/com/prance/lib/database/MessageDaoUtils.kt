package com.prance.lib.database

class MessageDaoUtils {

    private val mDao: MessageEntityDao = DaoManager.daoSession?.messageEntityDao!!

    fun saveMessage(message: MessageEntity): MessageEntity {
        val id = mDao.insert(message)
        message.id = id
        return message
    }

    fun getAllMessage(): MutableList<MessageEntity> {
        return mDao.loadAll()
    }

    fun getMessageByMsgId(msgId: String?): MessageEntity? {
        val list = mDao.queryRaw("""where ${MessageEntityDao.Properties.MsgId.columnName} = '$msgId'""")
        return if (list.isEmpty()) {
            null
        } else {
            list[0]
        }
    }

    fun deleteMessage(message: MessageEntity) {
        mDao.delete(message)
    }
}