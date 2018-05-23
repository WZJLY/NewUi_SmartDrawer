package com.example.newui_smartdrawer.util;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class Reagent {
    private String reagentId;
    private String reagentName;
    private String reagentAlias;
    private String reagentFormalName;
    private String reagentChemName;
    private int reagentType;
    private String reagentPurity;
    private String reagentSize;
    private String reagentTotalSize;
    private String reagentCreater;
    private String reagentGoodsID;
    private int reagentUnit;
    private String reagentDensity;
    private String reagentInvalidDate;
    private String cabinetId;
    private String drawerId;
    private String reagentPosition;
    private int status;
    private String reagentUser;

    public Reagent(String strReagentId, String strReagentName, String strReagentAlias,
                   String strReagentFormalName, String strReagentChemName, int iReagentType,
                   String strReagentPurity, String strReagentSize, String strReagentTotalSize,
                   String strReagentCreater, String strReagentGoodsID, int iReagentUnit,
                   String strReagentDensity, String strReagentInvalidDate, String strCabinetId,
                   String strDrawerId, String strReagentPos, int iStatus, String strReagentUser){
        /*
        * 试剂ID，试剂名称，试剂别名
        * 试剂学名，分子式，试剂类型(1-普通，2-管制)
        * 试剂纯度，试剂净含量，试剂总重量
        * 生成厂商，商品码，试剂单位(1-g，2-ml)
        * 试剂密度(单位为ml时使用)，试剂有效期，试剂柜id
        * 抽屉id，试剂位置，试剂状态(在位:1、取走:2、失效:3)、试剂使用者(取走试剂的人)        *
        * */
        this.reagentId = strReagentId;
        this.reagentName = strReagentName;
        this.reagentAlias = strReagentAlias;

        this.reagentFormalName = strReagentFormalName;
        this.reagentChemName = strReagentChemName;
        this.reagentType = iReagentType;

        this.reagentPurity = strReagentPurity;
        this.reagentSize = strReagentSize;
        this.reagentTotalSize = strReagentTotalSize;

        this.reagentCreater = strReagentCreater;
        this.reagentGoodsID = strReagentGoodsID;
        this.reagentUnit = iReagentUnit;

        this.reagentDensity = strReagentDensity;
        this.reagentInvalidDate = strReagentInvalidDate;
        this.cabinetId = strCabinetId;

        this.drawerId = strDrawerId;
        this.reagentPosition = strReagentPos;
        this.status = iStatus;
        this.reagentUser = strReagentUser;
    }

    public String getReagentId(){
        return this.reagentId;
    }

    public String getReagentName(){
        return this.reagentName;
    }

    public String getReagentPurity() {
        return this.reagentPurity;
    }

    public String getReagentCreater() {
        return this.reagentCreater;
    }

    public String getReagentSize() {
        return this.reagentSize;
    }

    public String getReagentUser() {
        return this.reagentUser;
    }

    public String getReagentInvalidDate(){
        return this.reagentInvalidDate;
    }

    public String getReagentCabinetId(){
        return this.cabinetId;
    }

    public String getDrawerId(){
        return this.drawerId;
    }

    public String getReagentPosition(){
        return this.reagentPosition;
    }


    public int getStatus(){
        return this.status;
    }
        public  void setStatus(int state){  this.status=state;}
    public int getReagentType() {
        return this.reagentType;
    }

    public String getReagentTotalSize(){return this.reagentTotalSize;}
    public int getReagentUnit(){
        return this.reagentUnit;
    }
    public String getReagentDensity(){return this.reagentDensity;}
}
