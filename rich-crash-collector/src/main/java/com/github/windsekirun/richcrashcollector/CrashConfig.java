package com.github.windsekirun.richcrashcollector;

import android.content.Context;

import com.github.windsekirun.richcrashcollector.item.LogLevel;

/**
 * RichCrashCollector
 * CrashConfig
 * Created by pyxis on 2017. 3. 17..
 */

@SuppressWarnings("WeakerAccess")
public class CrashConfig {
    private boolean displayDeviceInfo;
    private LogLevel logLevel;
    private String timeFormat;
    private String versionStr;
    private String packageName;
    private String logLocation;

    String getPackageName() {
        return packageName;
    }

    LogLevel getLogLevel() {
        return logLevel;
    }

    String getTimeFormat() {
        return timeFormat;
    }

    boolean isDisplayDeviceInfo() {
        return displayDeviceInfo;
    }

    String getVersionStr() {
        return versionStr;
    }

    String getLogLocation() {
        return logLocation;
    }

    private CrashConfig(Builder builder) {
        this.displayDeviceInfo = builder.displayDeviceInfo;
        this.logLevel = builder.logLevel;
        this.timeFormat = builder.timeFormat;
        this.packageName = builder.packageName;
        this.versionStr = builder.versionStr;
        this.logLocation = builder.logLocation;
    }

    @SuppressWarnings({"ConstantConditions", "unused"})
    public static class Builder {
        private boolean displayDeviceInfo = true;
        private LogLevel logLevel = LogLevel.MESSAGE;
        private String timeFormat = "yyyy-MM-dd (e) a hh:mm:ss.SSS";
        private String packageName = "com.github.windsekirun.richcrashcollector";
        private String versionStr = "1.0.0(1)";
        private String logLocation = null;

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

        public Builder setLogLocation(String logLocation) {
            this.logLocation = logLocation;
            return this;
        }

        public CrashConfig build(Context context) {
            if (logLocation == null)
                logLocation = context.getExternalFilesDir("crash").toString();

            return new CrashConfig(this);
        }
    }
}

