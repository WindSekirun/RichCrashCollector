package com.github.windsekirun.richcrashcollector;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

/**
 * RichCrashCollector
 * Created by pyxis on 2017. 3. 17..
 */

public class CrashCollector {

    public static void initCrashCollector(Application app) {
        Context context = app.getApplicationContext();
        int pid = android.os.Process.myPid();

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                if (appProcess.processName.equalsIgnoreCase(context.getPackageName())) {
                    // TODO: apply setDefaultUncaughtExceptionHandler into Process (in this case, specific pid which my application
                    // May i need some Builder pattern?
                    // I will check after i produced all possible code to customize options.
                }
            }
        }
    }
}
