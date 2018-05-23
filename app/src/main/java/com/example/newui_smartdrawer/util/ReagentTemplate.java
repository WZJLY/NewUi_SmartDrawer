package com.example.newui_smartdrawer.util;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class ReagentTemplate {
    private String reagentId;
    private String reagentName;
    private String reagentAlias;
    private String reagentFormalName;
    private String reagentChemName;
    private int reagentType;
    private String reagentPurity;
    private String reagentSize;
    private String reagentCreater;
    private String reagentGoodsID;
    private String reagentUnit;
    private String reagentDensity;

    public ReagentTemplate(String strReagentId, String strReagentName, String strReagentAlias,
                           String strReagentFormalName, String strReagentChemName, int iReagentType,
                           String strReagentPurity, String strReagentSize, String strReagentCreater,
                           String strReagentGoodsID, String strReagentUnit, String strReagentDensity){

        // id, 试剂类型ID,试剂名称,试剂别称
        // 试剂学名,分子式,试剂类型
        // 试剂纯度,试剂净含量,生产厂商
        // 商品码,计量单位(g或者ml),密度(g/cm3)
        this.reagentId = strReagentId;
        this.reagentName = strReagentName;
        this.reagentAlias = strReagentAlias;

        this.reagentFormalName = strReagentFormalName;
        this.reagentChemName = strReagentChemName;
        this.reagentType = iReagentType;

        this.reagentPurity = strReagentPurity;
        this.reagentSize = strReagentSize;

        this.reagentCreater = strReagentCreater;
        this.reagentGoodsID = strReagentGoodsID;
        this.reagentUnit = strReagentUnit;

        this.reagentDensity = strReagentDensity;
    }
    public String getReagentName()
    {
        return reagentName;
    }
    public String getReagentPurity()
    {
        return reagentPurity;
    }
    public String getReagentCreater() {return reagentCreater;}
    public String getReagentSize()
    {
        return reagentSize;
    }
    public String getReagentUnit() {return reagentUnit;}

    public int getReagentType()
    {
        return reagentType;
    }
    public String getReagentGoodsID()
    {
        return reagentGoodsID;
    }
    public String getReagentDensity() {return reagentDensity;}
public String getReagentAlias(){return reagentAlias;}

}

