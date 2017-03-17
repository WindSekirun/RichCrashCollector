package com.github.windsekirun.richcrashcollector;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.SystemClock;

import com.github.windsekirun.richcrashcollector.item.LogLevel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * RichCrashCollector
 * CrashHandler
 * Created by pyxis on 2017. 3. 17..
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private CrashHandler instance;

    private Thread.UncaughtExceptionHandler defaultExceptionHandler; // we need this object if CrashHandler doesn't collect logs properly
    private CrashConfig crashConfig;
    private Context context;
    private Calendar now;

    public CrashHandler getInstance(Context context, CrashConfig config) {
        if (instance == null)
            instance = new CrashHandler(context, config);

        return instance;
    }

    private CrashHandler(Context context, CrashConfig config) {
        this.context = context;
        this.crashConfig = config;
        now = Calendar.getInstance();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        now = Calendar.getInstance();
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

    private String writeLogIntoMarkdown(Throwable ex) throws PackageManager.NameNotFoundException {
        StringBuilder builder = new StringBuilder();
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat(crashConfig.getTimeFormat());

        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);

        try {
            Throwable cause = ex.getCause();
            cause.printStackTrace(printWriter);
        } catch (NullPointerException e) {
        } finally {
            printWriter.close();
        }

        String stackTrace = result.toString();

        builder.append("## Crash Log in ")
                .append(context.getPackageName())
                .append(getLineBreak())
                .append("### Application Info")
                .append(getLineBreak())
                .append("* Package Name: **")
                .append(context.getPackageName())
                .append("**")
                .append(getLineBreak())
                .append("* Version: **")
                .append(packageInfo.versionName)
                .append("(")
                .append(packageInfo.versionCode)
                .append(")")
                .append("**")
                .append(getLineBreak())
                .append(getLineBreak());

        if (crashConfig.isDisplayDeviceInfo()) {
            builder.append("### Device Info")
                    .append(getLineBreak())
                    .append("* Device: **")
                    .append(Build.DEVICE)
                    .append(" (a.k.a ")
                    .append(Build.PRODUCT)
                    .append(" or ")
                    .append(Build.MODEL)
                    .append(")**")
                    .append(getLineBreak())
                    .append("* Version: **")
                    .append(Build.VERSION.CODENAME)
                    .append(" (")
                    .append(Build.VERSION.SDK_INT)
                    .append(")**")
                    .append(getLineBreak())
                    .append("* Manufacturer: **")
                    .append(Build.MANUFACTURER)
                    .append(getLineBreak())
                    .append(getLineBreak());
        }

        builder.append("### Crash Info")
                .append(getLineBreak())
                .append("* When: **")
                .append(dateFormat.format(now.getTime()))
                .append("**")
                .append(getLineBreak())
                .append("* Message: **")
                .append(ex.getMessage())
                .append("**")
                .append(getLineBreak())
                .append("* Localized Message: **")
                .append(ex.getLocalizedMessage())
                .append("**")
                .append(getLineBreak())
                .append(getLineBreak());

        if (crashConfig.getLogLevel() == LogLevel.STACKTRACE) {
            builder.append("#### Stack Trace")
                    .append(getLineBreak())
                    .append("````")
                    .append(stackTrace)
                    .append("````")
                    .append(getLineBreak())
                    .append(getLineBreak());
        }

        builder.append("Send this document to Developers, it will help so much!")
                .append(getLineBreak())
                .append(getLineBreak())
                .append("Powered by [RichCrashCollector](https://github.com/pyxisdev/RichCrashCollector)")
                .append(getLineBreak());

        return builder.toString();
    }


    private String getLineBreak() {
        return System.getProperty("line.separator");
    }
}