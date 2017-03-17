package com.github.windsekirun.richcrashcollector;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

/**
 * RichCrashCollector
 * CrashCollector
 * Created by pyxis on 2017. 3. 17..
 */

public class CrashCollector {

    public static void initCrashCollector(Application app) {
        initCrashCollector(app, new CrashConfig.Builder().build());
    }

    public static void initCrashCollector(Application app, CrashConfig config) {
        Context context = app.getApplicationContext();
        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : activityManager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                if (process.processName.equalsIgnoreCase(context.getPackageName())) {
                    Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance(context, config));
                }
            }
        }
    }
}
