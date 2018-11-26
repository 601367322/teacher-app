package com.prance.teacher.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Activity 生命周期管理类
 * Created by shenbingbing on 16/6/1.
 */
public class ActivityLifeManager implements Application.ActivityLifecycleCallbacks {

    private static Stack<Activity> activityStack;
    private static ActivityLifeManager mInstance;

    private ActivityLifeManager() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
    }

    public static ActivityLifeManager getInstance() {
        if (mInstance == null) {
            synchronized (ActivityLifeManager.class) {
                if (mInstance == null) {
                    mInstance = new ActivityLifeManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        pushActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtils.i(activity.getComponentName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtils.i(activity.getComponentName());
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        popActivity(activity);
    }


    /**
     * 关闭其他所有
     *
     * @param clazz 过滤掉的Activity.Class
     */
    public void popAllActivity(List<Class> clazz) {
        for (int j = 0; j < clazz.size(); j++) {
            for (int i = 0; i < activityStack.size(); i++) {
                Activity activity = activityStack.get(i);
                if (activity.getClass().equals(clazz.get(j))) {
                    popActivity(activity);
                    continue;
                }
            }
        }
    }

    /**
     * 关闭最后一个打开的Activity
     */
    public void popActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 关闭指定Activity
     *
     * @param className Activity name
     */
    public void popActivity(String className) {
        ArrayList<Activity> ac = new ArrayList<Activity>();
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().getName().equals(className)) {
                ac.add(activityStack.get(i));
            }
        }
        if (ac.size() > 0) {
            for (Activity activity2 : ac) {
                activity2.finish();
                activityStack.remove(activity2);
                ac = null;
            }
        }
    }

    /**
     * 关闭指定Activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 关闭指定Activity
     *
     * @param clazz
     */
    public void popActivity(Class<?> clazz) {
        ArrayList<Activity> ac = new ArrayList<Activity>();
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(clazz)) {
                ac.add(activityStack.get(i));
            }
        }
        if (ac.size() > 0) {
            for (Activity activity2 : ac) {
                activity2.finish();
                activityStack.remove(activity2);
                ac = null;
            }
        }
    }

    /**
     * 关闭之前存在的相同的Activity
     *
     * @param clazz
     */
    public void popPreSameActivity(Class<?> clazz) {
        ArrayList<Activity> ac = new ArrayList<Activity>();
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(clazz)) {
                ac.add(activityStack.get(i));
            }
        }
        if (ac.size() > 1) {
            for (int i = 0; i < ac.size() - 1; i++) {
                ac.get(i).finish();
                activityStack.remove(ac.get(i));
            }
        }
        ac.clear();
    }

    /**
     * 获取最后一个打开的Activity
     *
     * @return
     */
    public Activity currentActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            Activity activity = activityStack.lastElement();
            return activity;
        } else {
            return null;
        }
    }

    /**
     * 将Activity加入队列
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 除了某个Activity，关闭其他所有
     *
     * @param clazz
     */
    public void popAllActivityExceptOne(Class clazz) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (clazz != null && activity.getClass().equals(clazz)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 关闭所有Activity
     */
    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 存在？
     *
     * @param name
     * @return
     */
    public boolean contains(String name) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int getActivitySize() {
        return activityStack.size();
    }
}
