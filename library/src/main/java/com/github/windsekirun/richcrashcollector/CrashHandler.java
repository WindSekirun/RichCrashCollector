package com.github.windsekirun.richcrashcollector;

import android.os.SystemClock;

/**
 * RichCrashCollector
 * CrashHandler
 * Created by pyxis on 2017. 3. 17..
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultExceptionHandler; // we need this object if CrashHandler doesn't collect logs properly

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && defaultExceptionHandler != null) {
            defaultExceptionHandler.uncaughtException(t, e);
        } else {
            SystemClock.sleep(3000);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // TODO: Save log, or device.. some info to files;
        return true;
    }
}
