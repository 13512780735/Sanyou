package com.pk4pk.baseappmoudle.utils;


import android.os.StrictMode;

public class AppInit {

    public static void initStrictMode(boolean isDebug){
        if(isDebug){
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects() //检查SQLiteCursor泄漏
                    .detectLeakedClosableObjects() //检查所有继承java.io.Closeable泄漏:如流没有关闭
                    .detectActivityLeaks() //检查Activity泄漏
                    .penaltyLog() //检测到问题时，将日志输出到Logcat
                    .penaltyDeath() //检测到问题时，直接崩溃
                    .build());
        }
    }
}
