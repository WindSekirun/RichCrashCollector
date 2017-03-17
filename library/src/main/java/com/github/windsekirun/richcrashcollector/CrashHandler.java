package com.github.windsekirun.richcrashcollector;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;

import com.github.windsekirun.richcrashcollector.item.LogLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * RichCrashCollector
 * CrashHandler
 * Created by pyxis on 2017. 3. 17..
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;
    private Thread.UncaughtExceptionHandler defaultExceptionHandler; // we need this object if CrashHandler doesn't collect logs properly
    private CrashConfig crashConfig;
    private Calendar now;

    static CrashHandler getInstance(CrashConfig config) {
        if (instance == null)
            instance = new CrashHandler(config);

        return instance;
    }

    private CrashHandler(CrashConfig config) {
        this.crashConfig = config;
        now = Calendar.getInstance();
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        now = Calendar.getInstance();
        if (!handleException(e) && defaultExceptionHandler != null) {
            defaultExceptionHandler.uncaughtException(t, null);
        } else {
            e.printStackTrace();
        }
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }

        new Thread() {
            @Override
            public void run() {
                saveLocal(writeLogIntoMarkdown(ex));
            }
        }.start();
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveLocal(String message) {
        String fileName = getFileName();
        File file = new File(crashConfig.getLogLocation(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                file.setWritable(Boolean.TRUE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(message.getBytes());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    @SuppressLint("InlinedApi")
    private String writeLogIntoMarkdown(Throwable ex) {
        StringBuilder builder = new StringBuilder();
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
                .append(crashConfig.getPackageName())
                .append(getLineBreak())
                .append("### Application Info")
                .append(getLineBreak())
                .append("* Package Name: **")
                .append(crashConfig.getPackageName())
                .append("**")
                .append(getLineBreak())
                .append("* Version: **")
                .append(crashConfig.getVersionStr())
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

    private String getFileName() {
        return "crash_" + getTimeForPrint() + ".log";
    }

    private static String getTimeForPrint() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
    }

    private String getLineBreak() {
        return System.getProperty("line.separator");
    }
}