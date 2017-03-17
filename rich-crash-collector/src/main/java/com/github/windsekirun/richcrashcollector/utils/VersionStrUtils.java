package com.github.windsekirun.richcrashcollector.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * VersionStrUtils
 * Created by pyxis on 2017. 3. 18..
 */

public class VersionStrUtils {

    public static String getVersionStr(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName + "(" + packageInfo.versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

}
