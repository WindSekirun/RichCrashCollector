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
    private String versionStr;
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public boolean isDisplayDeviceInfo() {
        return displayDeviceInfo;
    }

    public String getVersionStr() {
        return versionStr;
    }

    private CrashConfig(Builder builder) {
        this.displayDeviceInfo = builder.displayDeviceInfo;
        this.logLevel = builder.logLevel;
        this.timeFormat = builder.timeFormat;
        this.packageName = builder.packageName;
        this.versionStr = builder.versionStr;
    }

    public static class Builder {
        private boolean displayDeviceInfo = false;
        private LogLevel logLevel = LogLevel.MESSAGE;
        private String timeFormat = "yyyy-MM-dd (e) a hh:mm:ss.SSS";
        private String packageName;
        private String versionStr;

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

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder setVersionStr(String versionStr) {
            this.versionStr = versionStr;
            return this;
        }

        public CrashConfig build() {
            CrashConfig crashConfig = new CrashConfig(this);
            return crashConfig;
        }
    }
}

