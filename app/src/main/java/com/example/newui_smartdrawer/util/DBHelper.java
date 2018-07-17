package com.example.newui_smartdrawer.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smart_cabinet_0926.db";
    private static final int DATABASE_VERSION = 4;

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        //main Box
        db.execSQL("INSERT INTO box VALUES(?)", new Object[]{1});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE user add column userAccount VARCHAR");
                db.execSQL("ALTER TABLE user add column phoneNumber VARCHAR");
                db.execSQL("drop TABLE cabinet_no");
                db.execSQL("CREATE TABLE IF NOT EXISTS cabinet_no" +
                        "(_id INTEGER PRIMARY KEY,cabinet_no VARCHAR,cabinet_serviceCode VARCHAR)");
                db.execSQL("CREATE TABLE IF NOT EXISTS sysSeting"+
                    "(_id INTEGER PRIMARY KEY, serialNum VARCHAR, cameraVersion VARCHAR)");//理工
            case 2:
                db.execSQL("ALTER TABLE user add column statue VARCHAR");
                db.execSQL("ALTER TABLE drawer add column statue VARCHAR");//人员和chou抽屉状态
            case 3://需要新增加一张称重的表，试剂记录需要多一列试剂名
                db.execSQL("ALTER TABLE reagentUserRecord add column reagentName VARCHAR");
                db.execSQL("CREATE TABLE IF NOT EXISTS initialWeight"+
                        "(_id INTEGER PRIMARY KEY, weight  VARCHAR)");

        }
    }

    public void createTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, userId VARCHAR, userName VARCHAR, userPassword VARCHAR, userPower INTEGER,userAccount VARCHAR,phoneNumber VARCHAR,statue VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS box" +
                "(_id INTEGER PRIMARY KEY)");

        db.execSQL("CREATE TABLE IF NOT EXISTS drawer" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, drawerId INTEGER KEY, boxId INTEGER KEY, drawerSize INTEGER,statue VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS reagent" +
                "(_id INTEGER PRIMARY KEY, reagentId VARCHAR, reagentName VARCHAR, reagentAlias VARCHAR, " +
                "reagentFormalName VARCHAR, reagentChemName VARCHAR, reagentType INTEGER, " +
                "reagentPurity VARCHAR, reagentSize VARCHAR, reagentTotalSize VARCHAR, " +
                "reagentCreater VARCHAR, reagentGoodsID VARCHAR, reagentUnit INTEGER, " +
                "reagentDensity VARCHAR, reagentInvalidDate VARCHAR, cabinetId VARCHAR, " +
                "drawerId VARCHAR, reagentPosition VARCHAR, status INTEGER, reagentUser VARCHAR)");
        //db.execSQL("INSERT INTO reagent VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{1, "testId", "testName", "2019-09-13", "1", "1", "100", "R-6,C-1", "2000-01-13", 1, "纯净水"});

        // id, 试剂类型ID,试剂名称,试剂别称
        // 试剂学名,分子式,试剂类型
        // 试剂纯度,试剂净含量,生产厂商
        // 商品码,计量单位(g或者ml),密度(g/cm3)
        db.execSQL("CREATE TABLE IF NOT EXISTS reagentTemplate" +
                "(_id INTEGER PRIMARY KEY, reagentId VARCHAR, reagentName VARCHAR, reagentAlias VARCHAR, " +
                "reagentFormalName VARCHAR, reagentChemName VARCHAR, reagentType INTEGER, " +
                "reagentPurity VARCHAR, reagentSize VARCHAR, reagentCreater VARCHAR, " +
                "reagentGoodsID VARCHAR, reagentUnit INTEGER, reagentDensity VARCHAR)");

        //存放试剂编号
        db.execSQL("CREATE TABLE IF NOT EXISTS cabinet_no" +
                "(_id INTEGER PRIMARY KEY,cabinet_no VARCHAR,cabinet_serviceCode VARCHAR)");
        //试剂使用记录表       //加一列试剂名(2018/7/11)
        db.execSQL("CREATE TABLE IF NOT EXISTS reagentUserRecord" +
                "(_id INTEGER PRIMARY KEY, reagentId VARCHAR,operationType INTEGER, operationTime VARCHAR, " +
                "operator VARCHAR, reagentTotalSize VARCHAR, reagentSize VARCHAR, consumption VARCHAR,reagentName VARCHAR)");
        //试剂报废表
        db.execSQL("CREATE TABLE IF NOT EXISTS scrapReagent" +
                "(_id INTEGER PRIMARY KEY, reagentId VARCHAR, reagentName VARCHAR, reagentAlias VARCHAR, " +
                "reagentFormalName VARCHAR, reagentChemName VARCHAR, reagentType INTEGER, " +
                "reagentPurity VARCHAR, reagentSize VARCHAR, reagentTotalSize VARCHAR, " +
                "reagentCreater VARCHAR, reagentGoodsID VARCHAR, reagentUnit INTEGER, " +
                "reagentDensity VARCHAR, reagentInvalidDate VARCHAR, cabinetId VARCHAR, " +
                "drawerId VARCHAR, reagentPosition VARCHAR, status INTEGER, reagentUser VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS sysSeting"+
                "(_id INTEGER PRIMARY KEY, serialNum VARCHAR, cameraVersion VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS initialWeight"+
                "(_id INTEGER PRIMARY KEY, weight  VARCHAR)");
    }






}
