package com.prance.lib.database;

import android.content.Context;

import com.blankj.utilcode.util.Utils;

public class KeyPadDaoUtils {

    private static final String TAG = KeyPadDaoUtils.class.getSimpleName();
    private DaoManager mManager;
    private KeyPadEntityDao mDao;

    public KeyPadDaoUtils() {
        mManager = DaoManager.getInstance();
        mManager.init(Utils.getApp());
        mDao = mManager.getDaoSession().getKeyPadEntityDao();
    }


}