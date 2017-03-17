package com.github.windsekirun.richcrashcollector.sample;

import android.app.Application;

import com.github.windsekirun.richcrashcollector.CrashCollector;
import com.github.windsekirun.richcrashcollector.CrashConfig;
import com.github.windsekirun.richcrashcollector.item.LogLevel;
import com.github.windsekirun.richcrashcollector.utils.VersionStrUtils;

/**
 * Created by pyxis on 2017. 3. 18..
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashConfig config = new CrashConfig.Builder()
                .setDisplayDeviceInfo(true)
                .setLogLevel(LogLevel.STACKTRACE)
                .setLogLocation(getExternalFilesDir("crash").toString())
                .setPackageName(getPackageName())
                .setTimeFormat("yyyy-MM-dd (E) a hh:mm:ss.SSS")
                .setVersionStr(VersionStrUtils.getVersionStr(this))
                .build(this);

        CrashCollector.initCrashCollector(this, config);
    }
}
