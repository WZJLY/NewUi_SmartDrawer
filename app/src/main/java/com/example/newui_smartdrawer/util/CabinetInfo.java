package com.example.newui_smartdrawer.util;

public class CabinetInfo {
    public String CabinetNo;
    public String CabinetServiceCode;
    public String getCabinetNo() {
        return CabinetNo;
    }
    public String getCabinetServiceCode(){
        return CabinetServiceCode;
    }
    public  CabinetInfo(String cabinetNo,String cabinetServiceCode) {
        this.CabinetNo = cabinetNo;
        this.CabinetServiceCode=cabinetServiceCode;
    }
}
