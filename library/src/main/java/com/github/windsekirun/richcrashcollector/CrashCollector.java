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
        initCrashCollector(app, new CrashConfig.Builder().build(app.getApplicationContext()));
    }

    public static void initCrashCollector(Application app, CrashConfig config) {
        Context context = app.getApplicationContext();
        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getRunningAppProcesses().stream()
                .filter(process -> process.pid == pid)
                .filter(process -> process.processName.equalsIgnoreCase(context.getPackageName())).forEach(process -> {
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance(config));
        });
    }
}
