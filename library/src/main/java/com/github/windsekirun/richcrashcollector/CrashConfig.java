package com.github.windsekirun.richcrashcollector;

import com.github.windsekirun.richcrashcollector.item.LogLevel;

/**
 * RichCrashCollector
 * CrashConfig
 * Created by pyxis on 2017. 3. 17..
 */

public class CrashConfig {
    private boolean displayDeviceInfo;
    private LogLevel logLevel;
    private String timeFormat;

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public boolean isDisplayDeviceInfo() {
        return displayDeviceInfo;
    }

    private CrashConfig(Builder builder) {
        this.displayDeviceInfo = builder.displayDeviceInfo;
        this.logLevel = builder.logLevel;
        this.timeFormat = builder.timeFormat;
    }

    public static class Builder {
        private boolean displayDeviceInfo = false;
        private LogLevel logLevel = LogLevel.MESSAGE;
        private String timeFormat = "yyyy-MM-dd (e) a hh:mm:ss.SSS";

        public Builder setDisplayDeviceInfo(boolean displayDeviceInfo) {
            this.displayDeviceInfo = displayDeviceInfo;
            return this;
        }

        public Builder setLogLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder setTimeFormat(String timeFormat) {
            this.timeFormat = timeFormat;
            return this;
        }

        public CrashConfig build() {
            CrashConfig crashConfig = new CrashConfig(this);
            return crashConfig;
        }
    }
}

