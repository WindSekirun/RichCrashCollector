package com.github.windsekirun.richcrashcollector;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;

import com.github.windsekirun.richcrashcollector.item.DocumentType;
import com.github.windsekirun.richcrashcollector.item.LogLevel;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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

class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
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
        handleException(e);
    }

    private void handleException(final Throwable ex) {
        if (ex == null) {
            return;
        }

        new Thread() {
            @Override
            public void run() {
                saveLocal(writeLogIntoMarkdown(ex));
            }
        }.start();
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

        byte[] messageBytes;
        if (crashConfig.getDocumentType() == DocumentType.HTML) {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(message);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            messageBytes = renderer.render(document).getBytes();
        } else {
            messageBytes = message.getBytes();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(messageBytes);
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

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
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
                    .append(Build.MODEL)
                    .append(" (a.k.a ")
                    .append(Build.PRODUCT)
                    .append(" or ")
                    .append(Build.DEVICE)
                    .append(")**")
                    .append(getLineBreak())
                    .append("* Version: **")
                    .append(Build.VERSION.SDK)
                    .append(" (")
                    .append(Build.VERSION.SDK_INT)
                    .append(")**")
                    .append(getLineBreak())
                    .append("* Manufacturer: **")
                    .append(Build.MANUFACTURER)
                    .append("**")
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
                    .append(getLineBreak())
                    .append(stackTrace)
                    .append("````")
                    .append(getLineBreak())
                    .append(getLineBreak());
        }

        return builder.toString();
    }

    private String getFileName() {
        if (crashConfig.getDocumentType() == DocumentType.HTML)
            return "crash_" + getTimeForPrint() + ".html";
        else
            return "crash_" + getTimeForPrint() + ".md";
    }

    private static String getTimeForPrint() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
    }

    private String getLineBreak() {
        return System.getProperty("line.separator");
    }
}