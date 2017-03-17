package com.github.windsekirun.richcrashcollector;

import com.github.windsekirun.richcrashcollector.item.LogLevel;

/**
 * RichCrashCollector
 * CrashConfig
 * Created by pyxis on 2017. 3. 17..
 */

public class CrashConfig {
    private boolean displayDeviceInfo;
    private LogLevel logLevel = LogLevel.MESSAGE;
    private String timeFormat = null;
    private String logLocation = null;

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getLogLocation() {
        return logLocation;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public boolean isDisplayDeviceInfo() {
        return displayDeviceInfo;
    }
}

