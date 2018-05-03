package com.likeits.sanyou.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于管理Activity的类。在程序退出的时候清除所有的Activity
 *
 * @author mo
 */
public class MyActivityManager {

    private List<Activity> activities = null;
    private static MyActivityManager instance;


    private MyActivityManager() {
        activities = new ArrayList<Activity>();
    }

    public static MyActivityManager getInstance() {
        if (instance == null) {
            instance = new MyActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activities != null && activities.size() > 0) {
            if (!activities.contains(activity)) {
                activities.add(activity);
            }
        } else {
            activities.add(activity);
        }
    }


    /**
     * 移除Activity到容器中
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
        activity.finish();
    }

    /**
     * 返回主界面
     */
    public void backToMain() {
        if (activities != null && activities.size() > 0) {
            for (int i = 0; i < activities.size(); i++) {
                activities.get(i).finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activities.size(); i < size; i++) {
            if (null != activities.get(i)) {
                activities.get(i).finish();
            }
        }
        activities.clear();
    }

    /**
     * 注销系统
     *
     * @param context
     */
    public void logout(final Context context) {

    }


    /**
     * 后台运行，不退出
     *
     * @param context
     */
    public void moveTaskToBack(Context context) {
        try {
            for (Activity activity : activities) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((Activity) context).moveTaskToBack(false);
        }
    }

    /**
     * 注销或退出客户端时注销用户在服务端的登录
     */
    @SuppressWarnings("unused")
    private void doLogout(Context mContext) {
        // String url = ConfigApp.HC_LOGOUT;
        // String userId = UtilPreference.getStringValue(mContext, "userId");
        // RequestParams params = new RequestParams();
        // params.add("userId", userId);
        //
        // HttpUtil.get(url, params, new RequestListener() {
        //
        // @Override
        // public void success(String response) {
        // Log.info("MyActivityManager", "注销成功：" + response);
        // }
        //
        // @Override
        // public void failed(Throwable error) {
        // Log.info("MyActivityManager", "注销失败：" + error.getMessage());
        // }
        // });

    }

    /**
     * 应用程序完全退出
     *
     * @param context 当前上下文
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context
                    .ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            MyActivityManager.getInstance().finishAllActivity();
            System.exit(0);

        } catch (Exception e) {
        }
    }

    /**
     * 退出应用
     *
     * @param context
     */
    public void exitSelf(Context context) {
        try {
            appExit(context);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            // 忽略这里的错误，不需要关注系统退出时的错误
        }
    }

}
