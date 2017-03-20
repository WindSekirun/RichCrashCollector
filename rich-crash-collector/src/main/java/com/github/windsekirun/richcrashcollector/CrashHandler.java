package com.github.windsekirun.richcrashcollector;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import com.github.windsekirun.richcrashcollector.item.DocumentType;
import com.github.windsekirun.richcrashcollector.item.LogLevel;
import com.github.windsekirun.richcrashcollector.item.RichCrashModel;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.nustaq.serialization.FSTConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * RichCrashCollector
 * CrashHandler
 * Created by pyxis on 2017. 3. 17..
 */

class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;
    private CrashConfig crashConfig;
    private Calendar now;
    private RichCrashModel richCrashModel;

    static CrashHandler getInstance(CrashConfig config) {
        if (instance == null)
            instance = new CrashHandler(config);

        return instance;
    }

    private CrashHandler(CrashConfig config) {
        this.crashConfig = config;
        now = Calendar.getInstance();
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

        richCrashModel = generateRichCrashModel(ex);
        new Thread() {
            @Override
            public void run() {
                saveLocal(writeLogIntoMarkdown());
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
        } else if (crashConfig.getDocumentType() == DocumentType.JSON) {
            FSTConfiguration conf = FSTConfiguration.createJsonConfiguration(true, false);
            messageBytes = conf.asByteArray(richCrashModel);
        } else {
            messageBytes = message.getBytes();
        }

        Log.d("messageBytes", Arrays.toString(messageBytes));

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

    private RichCrashModel generateRichCrashModel(Throwable ex) {
        RichCrashModel richCrashModel = new RichCrashModel();
        richCrashModel.setConfig(crashConfig);
        richCrashModel.setDeviceInfo(new Build());
        richCrashModel.setThrowable(ex);
        richCrashModel.setTime(now.getTime(), crashConfig);
        return richCrashModel;
    }

    @SuppressWarnings("EmptyCatchBlock")
    @SuppressLint("InlinedApi")
    private String writeLogIntoMarkdown() {
        StringBuilder builder = new StringBuilder();

        builder.append("## Crash Log in ")
                .append(richCrashModel.getPackageName())
                .append(getLineBreak())
                .append("### Application Info")
                .append(getLineBreak())
                .append("* Package Name: **")
                .append(richCrashModel.getPackageName())
                .append("**")
                .append(getLineBreak())
                .append("* Version: **")
                .append(richCrashModel.getVersionStr())
                .append("**")
                .append(getLineBreak())
                .append(getLineBreak());

        if (crashConfig.isDisplayDeviceInfo()) {
            builder.append("### Device Info")
                    .append(getLineBreak())
                    .append("* Device: **")
                    .append(richCrashModel.getModelStr())
                    .append(" (a.k.a ")
                    .append(richCrashModel.getProductStr())
                    .append(" or ")
                    .append(richCrashModel.getDeviceStr())
                    .append(")**")
                    .append(getLineBreak())
                    .append("* Version: **")
                    .append(richCrashModel.getSdkStr())
                    .append(" (")
                    .append(richCrashModel.getSdkNum())
                    .append(")**")
                    .append(getLineBreak())
                    .append("* Manufacturer: **")
                    .append(richCrashModel.getManufacturerStr())
                    .append("**")
                    .append(getLineBreak())
                    .append(getLineBreak());
        }

        builder.append("### Crash Info")
                .append(getLineBreak())
                .append("* When: **")
                .append(richCrashModel.getTimeStr())
                .append("**")
                .append(getLineBreak())
                .append("* Message: **")
                .append(richCrashModel.getMessage())
                .append("**")
                .append(getLineBreak())
                .append("* Localized Message: **")
                .append(richCrashModel.getLocalizedMessage())
                .append("**")
                .append(getLineBreak())
                .append(getLineBreak());

        if (crashConfig.getLogLevel() == LogLevel.STACKTRACE) {
            builder.append("#### Stack Trace")
                    .append(getLineBreak())
                    .append("````")
                    .append(getLineBreak())
                    .append(richCrashModel.getStackTrace())
                    .append("````")
                    .append(getLineBreak())
                    .append(getLineBreak());
        }

        return builder.toString();
    }

    private String getFileName() {
            return "crash_" + getTimeForPrint() + getExtension();
    }

    private String getExtension() {
        switch (crashConfig.getDocumentType()) {
            default:
            case MARKDOWN:
                return ".md";
            case HTML:
                return ".html";
            case JSON:
                return ".json";
        }
    }

    private static String getTimeForPrint() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
    }

    private String getLineBreak() {
        return System.getProperty("line.separator");
    }
}