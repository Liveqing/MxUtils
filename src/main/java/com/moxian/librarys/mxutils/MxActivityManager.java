package com.moxian.librarys.mxutils;

/**
 * @author LQ
 * 管理Activity的类
 * 在Application中注册
 *
 *  registerActivityLifecycleCallbacks(MxActivityManager.getActivityLifecycleCallbacks());
 *  然后就可以在任何地方
 *  MxActivityManager.getInstance().currentActivity()获取当前最顶层activity
 *  MxActivityManager.getInstance().closeActivity(Activity activity)关闭activity
 *  MxActivityManager.getInstance().closeAllActivity()关闭所有activity
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.Stack;

@TargetApi(14)
public class MxActivityManager {
    private static Stack<Activity> activityStack = new Stack<>();
    private static final MxActivityLifecycleCallbacks instance = new MxActivityLifecycleCallbacks();
    private static class MxActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            activityStack.remove(activity);
            activityStack.push(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityStack.remove(activity);
        }
    }

    public static Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks(){
        return instance;
    }

    /**
     * 获得当前栈顶Activity
     *
     * @return
     */
    public static Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.isEmpty())
            activity = activityStack.peek();
        return activity;
    }

    /**
     * 主动退出Activity
     *
     * @param activity
     */
    public static void closeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void closeAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (null == activity) {
                break;
            }
            closeActivity(activity);
        }
    }

    /**
     * close a specific activity by its complete name.
     *
     * @param name For example: com.jude.utils.Activity_B
     */
    public static void closeActivityByName(String name) {
        int index = activityStack.size() - 1;

        while (true) {
            Activity activity = activityStack.get(index);

            if (null == activity) {
                break;
            }

            String activityName = activity.getComponentName().getClassName();
            if (!TextUtils.equals(name, activityName)) {
                index--;
                if (index < 0) {//avoid index out of bound.
                    break;
                }
                continue;
            }
            closeActivity(activity);
            break;
        }
    }

    /**
     * 获得当前ACTIVITY 名字
     */
    public static String getCurrentActivityName() {
        Activity activity = currentActivity();
        String name = "";
        if (activity != null) {
            name = activity.getComponentName().getClassName().toString();
        }
        return name;
    }

    public static Stack<Activity> getActivityStack(){
        Stack<Activity> stack = new Stack<>();
        stack.addAll(activityStack);
        return stack;
    }

}

