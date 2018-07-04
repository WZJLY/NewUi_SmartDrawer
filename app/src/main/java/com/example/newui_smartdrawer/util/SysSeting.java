package com.example.newui_smartdrawer.util;

/**
 * Created by WZJ on 2018/3/28.
 */

public class SysSeting {
    public String CameraVersion;
    public String SerialNum;
    public String getCameraVersion() {
        return this.CameraVersion;
    }
    public String getSerialNum(){return SerialNum;}
    public  SysSeting(String serialNum,String cameraVersion) {
        this.CameraVersion = cameraVersion;
        this.SerialNum=serialNum;
    }
}
