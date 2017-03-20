package com.github.windsekirun.richcrashcollector.item;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import com.github.windsekirun.richcrashcollector.CrashConfig;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

/**
 * RichCrashCollector
 * RichCrashModel
 * Created by DonggilSeo on 2017-03-20.
 */

public class RichCrashModel implements Serializable, Cloneable {
    private static final long serialVersionUID = 44612820179919067L;

    private String packageName;
    private String versionStr;
    private String modelStr;
    private String productStr;
    private String deviceStr;
    private String sdkStr;
    private int sdkNum;
    private String manufacturerStr;
    private String timeStr;
    private String message;
    private String localizedMessage;
    private String stackTrace;

    public int getSdkNum() {
        return sdkNum;
    }

    public String getDeviceStr() {
        return deviceStr;
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public String getManufacturerStr() {
        return manufacturerStr;
    }

    public String getMessage() {
        return message;
    }

    public String getModelStr() {
        return modelStr;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getProductStr() {
        return productStr;
    }

    public String getSdkStr() {
        return sdkStr;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getVersionStr() {
        return versionStr;
    }

    public void setThrowable(Throwable ex) {
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

        this.message = ex.getMessage();
        this.localizedMessage = ex.getLocalizedMessage();
        this.stackTrace = result.toString();
    }

    public void setDeviceInfo(Build build) {
        this.modelStr = Build.MODEL;
        this.productStr = Build.PRODUCT;
        this.deviceStr = Build.DEVICE;
        this.sdkStr = Build.VERSION.SDK;
        this.sdkNum = Build.VERSION.SDK_INT;
        this.manufacturerStr = Build.MANUFACTURER;
    }

    public void setConfig(CrashConfig config) {
        this.packageName = config.getPackageName();
        this.versionStr = config.getVersionStr();
    }

    public void setTime(Date date, CrashConfig crashConfig) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(crashConfig.getTimeFormat());
        this.timeStr = dateFormat.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RichCrashModel that = (RichCrashModel) o;

        if (getSdkNum() != that.getSdkNum()) return false;
        if (getPackageName() != null ? !getPackageName().equals(that.getPackageName()) : that.getPackageName() != null)
            return false;
        if (getVersionStr() != null ? !getVersionStr().equals(that.getVersionStr()) : that.getVersionStr() != null)
            return false;
        if (getModelStr() != null ? !getModelStr().equals(that.getModelStr()) : that.getModelStr() != null)
            return false;
        if (getProductStr() != null ? !getProductStr().equals(that.getProductStr()) : that.getProductStr() != null)
            return false;
        if (getDeviceStr() != null ? !getDeviceStr().equals(that.getDeviceStr()) : that.getDeviceStr() != null)
            return false;
        if (getSdkStr() != null ? !getSdkStr().equals(that.getSdkStr()) : that.getSdkStr() != null)
            return false;
        if (getManufacturerStr() != null ? !getManufacturerStr().equals(that.getManufacturerStr()) : that.getManufacturerStr() != null)
            return false;
        if (getTimeStr() != null ? !getTimeStr().equals(that.getTimeStr()) : that.getTimeStr() != null)
            return false;
        if (getMessage() != null ? !getMessage().equals(that.getMessage()) : that.getMessage() != null)
            return false;
        if (getLocalizedMessage() != null ? !getLocalizedMessage().equals(that.getLocalizedMessage()) : that.getLocalizedMessage() != null)
            return false;
        return getStackTrace() != null ? getStackTrace().equals(that.getStackTrace()) : that.getStackTrace() == null;

    }

    @Override
    public int hashCode() {
        int result = getPackageName() != null ? getPackageName().hashCode() : 0;
        result = 31 * result + (getVersionStr() != null ? getVersionStr().hashCode() : 0);
        result = 31 * result + (getModelStr() != null ? getModelStr().hashCode() : 0);
        result = 31 * result + (getProductStr() != null ? getProductStr().hashCode() : 0);
        result = 31 * result + (getDeviceStr() != null ? getDeviceStr().hashCode() : 0);
        result = 31 * result + (getSdkStr() != null ? getSdkStr().hashCode() : 0);
        result = 31 * result + getSdkNum();
        result = 31 * result + (getManufacturerStr() != null ? getManufacturerStr().hashCode() : 0);
        result = 31 * result + (getTimeStr() != null ? getTimeStr().hashCode() : 0);
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (getLocalizedMessage() != null ? getLocalizedMessage().hashCode() : 0);
        result = 31 * result + (getStackTrace() != null ? getStackTrace().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RichCrashModel{" +
                "packageName='" + packageName + '\'' +
                ", versionStr='" + versionStr + '\'' +
                ", modelStr='" + modelStr + '\'' +
                ", productStr='" + productStr + '\'' +
                ", deviceStr='" + deviceStr + '\'' +
                ", sdkStr='" + sdkStr + '\'' +
                ", sdkNum=" + sdkNum +
                ", manufacturerStr='" + manufacturerStr + '\'' +
                ", timeStr='" + timeStr + '\'' +
                ", message='" + message + '\'' +
                ", localizedMessage='" + localizedMessage + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }
}
