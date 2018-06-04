package com.example.newui_smartdrawer.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private String DBOPERATION = "DBManager";

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void tableUpgrade(){
        helper.createTable(db);
    }


    //----------------------------------user manage begin----------------------------//
    public void DeleteDBUser(String tableName){

    }

    public void deleteAccountByUserName(String userName){
        db.delete("user", "userName == ?", new String[]{ userName });
    }
    public void addAccount(UserAccount user) {
        if(!isAccountExist(user.userName)){
            db.execSQL("INSERT INTO user VALUES(null, ?, ?, ?, ?, ?, ?,?)", new Object[]{user.userId, user.userName, user.userPassword, user.userPower, user.userAccount, user.phoneNumber,user.statue});
            Log.i(DBOPERATION, "Insert Success");
        }
        else{
            Log.i(DBOPERATION, "already exist!");
        }
    }

    public void deleteAccountByUserId(String userId){
        db.delete("user", "userId == ?", new String[]{ userId });
    }

    public void deleteAccount(UserAccount user) {
        db.delete("user", "userName == ?", new String[]{String.valueOf(user.userName)});
    }

    public void updateAccount(UserAccount user) {

    }
    public void updateStatueByUserName(String strUserName,String userStatue){

        ContentValues cv = new ContentValues();
        cv.put("userName", strUserName);
        cv.put("statue",userStatue);
        db.update("user", cv, "userName=?", new String[] { strUserName });

    }
    public boolean isAccountExist(String strUserName) {
        Cursor cursor = db.query("user", new String[] {"userId", "userName", "userPassword"}, "userName=?", new String[] { strUserName }, null, null, null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public boolean isAccountExist(String strUserName, String strUserPWD) {
        Cursor cursor = db.query("user", new String[] {"userId", "userName", "userPassword", "userPower","userAccount","phoneNumber"}, "userName=? AND userPassword=?", new String[] { strUserName, strUserPWD }, null, null, null);
        if (cursor.moveToNext()) {
            Log.i(DBOPERATION, "find this user in DB - name:" + cursor.getString(cursor.getColumnIndex("userName")) + "\t password:" + cursor.getString((cursor.getColumnIndex("userPower"))));
            return true;
        }
        return false;
    }

    public UserAccount getUserAccountByUserName(String strUserName){
        Cursor cursor = db.query("user", new String[] {"userId", "userName", "userPassword", "userPower","userAccount","phoneNumber","statue"}, "userName=?", new String[] { strUserName }, null, null, null);
        cursor.moveToNext();
        UserAccount userInfo = new UserAccount(
                cursor.getString(cursor.getColumnIndex("userId")),
                cursor.getString(cursor.getColumnIndex("userName")),
                cursor.getString(cursor.getColumnIndex("userPassword")),
                parseInt(cursor.getString(cursor.getColumnIndex("userPower"))),
                cursor.getString(cursor.getColumnIndex("userAccount")),
                cursor.getString(cursor.getColumnIndex("phoneNumber")),
                cursor.getString(cursor.getColumnIndex("statue"))
        );
        return userInfo;
    }

    public void updateAccountByUserName(String strUserName, String strUserId, String strUserPWD, int strUserPower,String strUserAccount,String strPhoneNumber){
        ContentValues cv = new ContentValues();
        cv.put("userId", strUserId);
        cv.put("userName", strUserName);
        cv.put("userPassword", strUserPWD);
        cv.put("userPower", strUserPower);
        cv.put("userAccount",strUserAccount);
        cv.put("phoneNumber",strPhoneNumber);
        db.update("user", cv, "userName=?", new String[] { strUserName });
    }

    public UserAccount getUserAccount(String strUserName, String strUserPWD){
        if(!isAccountExist(strUserName, strUserPWD)){
            Log.e(DBOPERATION, "user is not exist! error from getUserPower");
        }
        Cursor cursor = db.query("user", new String[] {"userId", "userName", "userPassword", "userPower","userAccount","phoneNumber","statue"}, "userName=? AND userPassword=?", new String[] { strUserName, strUserPWD }, null, null, null);
        cursor.moveToNext();
        UserAccount userInfo = new UserAccount(
                cursor.getString(cursor.getColumnIndex("userId")),
                cursor.getString(cursor.getColumnIndex("userName")),
                cursor.getString(cursor.getColumnIndex("userPassword")),
                parseInt(cursor.getString(cursor.getColumnIndex("userPower"))),
                cursor.getString(cursor.getColumnIndex("userAccount")),
                cursor.getString(cursor.getColumnIndex("phoneNumber")),
                cursor.getString(cursor.getColumnIndex("statue"))
        );
        return userInfo;
    }

    public boolean queryUserByUserId(String strUserId){
        Cursor cursor = db.query("user", new String[] {"userName", "userPassword", "userPower"}, "userId=?", new String[] { strUserId }, null, null, null);
        if (cursor.moveToNext()) {
            Log.i(DBOPERATION, "find this user in DB - name:" + cursor.getString(cursor.getColumnIndex("userName")) + "\t password:" + cursor.getString((cursor.getColumnIndex("userPower"))));
            return true;
        }
        return false;
    }

    public ArrayList<UserAccount> getUsers(){
        Cursor cursor = db.rawQuery("select * from user", null);
        ArrayList<UserAccount> arrListUsers = new ArrayList<>();
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                UserAccount userAccount = new UserAccount(
                        cursor.getString(cursor.getColumnIndex("userId")),
                        cursor.getString(cursor.getColumnIndex("userName")),
                        cursor.getString(cursor.getColumnIndex("userPassword")),
                        parseInt(cursor.getString(cursor.getColumnIndex("userPower"))),
                        cursor.getString(cursor.getColumnIndex("userAccount")),
                        cursor.getString(cursor.getColumnIndex("phoneNumber")),
                        cursor.getString(cursor.getColumnIndex("statue"))
                );
                arrListUsers.add(userAccount);
                cursor.moveToNext();
            }
        }
        return arrListUsers;
    }
    //----------------------------------user manage end----------------------------//

    //----------------------------------box manage begin----------------------------//
    public ArrayList<Box> getBoxes(){
        Cursor cursor = db.rawQuery("select * from box",null);
        ArrayList<Box> arrListBoxes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                arrListBoxes.add(new Box(cursor.getInt(cursor.getColumnIndex("_id"))));
                cursor.moveToNext();
            }
        }
        return arrListBoxes;
    }
    public void addBox(int boxId){
        Cursor cursor = db.query("box", new String[] {"_id"}, "_id=?", new String[] { boxId + ""}, null, null, null);
        if(cursor.moveToNext()){
            Log.e("DB_ADD_BOX_ERROR", "The Id of Box Already Exist");
        }else{
            db.execSQL("INSERT INTO box VALUES(?)", new Object[]{boxId});
        }
    }
    public void deleteBox(int boxId){
        db.delete("box", "_id=?", new String[]{boxId + ""});
        deleteDrawer(boxId);
    }

    //----------------------------------box manage end----------------------------//



    //----------------------------------drawer manage begin----------------------------//
    public ArrayList<Drawer> getDrawers(){
        Cursor cursor = db.rawQuery("select * from drawer",null);
        ArrayList<Drawer> arrListDrawers = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                arrListDrawers.add(new Drawer(cursor.getInt(cursor.getColumnIndex("drawerId")), cursor.getInt(cursor.getColumnIndex("boxId")),  cursor.getInt(cursor.getColumnIndex("drawerSize")),cursor.getString(cursor.getColumnIndex("statue"))));
                cursor.moveToNext();
            }
        }
        return arrListDrawers;
    }
    public void addDrawer(int drawerId, int boxId, int drawerSize,String statue){
        db.execSQL("INSERT INTO drawer VALUES(null, ?, ?, ?, ?)", new Object[]{drawerId, boxId, drawerSize,statue});
    }
    public void deleteDrawer(int drawerId, int boxId){
        db.delete("drawer", "drawerId=? AND boxId=?", new String[]{drawerId + "", boxId + ""});
    }
    public void deleteDrawer(int boxId){
        db.delete("drawer", "boxId=?", new String[]{boxId + ""});
    }
    public Drawer getDrawerByDrawerId(int strDrawerId,int strBoxID){
        Cursor cursor = db.query("drawer", new String[] {"drawerId", "boxId", "drawerSize","statue"}, "drawerId=? AND boxId=?", new String[] {strDrawerId+"",strBoxID+""}, null, null, null);
        cursor.moveToNext();
        Drawer drawerInfo = new Drawer(
                parseInt(cursor.getString(cursor.getColumnIndex("drawerId"))),
               parseInt(cursor.getString(cursor.getColumnIndex("boxId"))),
               parseInt(cursor.getString(cursor.getColumnIndex("drawerSize"))),
                cursor.getString(cursor.getColumnIndex("statue"))
        );
        return drawerInfo;
    }

//    public void updateDrawerStatue(int drawerId, int boxId,String statue){
//        ContentValues data=new ContentValues();
//        data.put("drawerId",drawerId);
//        data.put("boxId",boxId);
//        data.put("statue", statue);
//        db.update("drawer", data, "drawerId=? and boxId=?", new String[]{drawerId + "", boxId + ""});
//    }

    public void updateDrawer(int drawerId, int boxId, int drawerSize,String statue){
        ContentValues data=new ContentValues();
        data.put("drawerId",drawerId);
        data.put("boxId",boxId);
        data.put("drawerSize", drawerSize);
        data.put("statue",statue);
        db.update("drawer", data, "drawerId=? and boxId=?", new String[]{drawerId + "", boxId + ""});
    }
    //----------------------------------drawer manage end----------------------------//
/*
*
*
*  db.execSQL("CREATE TABLE IF NOT EXISTS reagent" +
                "(_id INTEGER PRIMARY KEY,
                 reagentId VARCHAR, reagentName VARCHAR, reagentAlias VARCHAR,
                 reagentFormalName VARCHAR, reagentChemName VARCHAR, reagentType INTEGER,
                 reagentPurity VARCHAR, reagentSize VARCHAR, reagentTotalSize VARCHAR,
                 reagentCreater VARCHAR, reagentGoodsID VARCHAR, reagentUnit INTEGER,
                 reagentDensity VARCHAR, reagentInvalidDate VARCHAR" cabinetId VARCHAR,
                 drawerId VARCHAR, reagentSize VARCHAR, reagentPosition VARCHAR,
                  status INTEGER, reagentUser VARCHAR)");
* */
    public ArrayList<Reagent> getReagents(){
        Cursor cursor = db.rawQuery("select * from reagent order by reagentName",null);
        ArrayList<Reagent> arrListReagents = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Reagent reagent = new Reagent(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getString(cursor.getColumnIndex("reagentName")), cursor.getString(cursor.getColumnIndex("reagentAlias")),
                        cursor.getString(cursor.getColumnIndex("reagentFormalName")), cursor.getString(cursor.getColumnIndex("reagentChemName")), cursor.getInt(cursor.getColumnIndex("reagentType")),
                        cursor.getString(cursor.getColumnIndex("reagentPurity")), cursor.getString(cursor.getColumnIndex("reagentSize")), cursor.getString(cursor.getColumnIndex("reagentTotalSize")),
                        cursor.getString(cursor.getColumnIndex("reagentCreater")), cursor.getString(cursor.getColumnIndex("reagentGoodsID")), cursor.getInt(cursor.getColumnIndex("reagentUnit")),
                        cursor.getString(cursor.getColumnIndex("reagentDensity")), cursor.getString(cursor.getColumnIndex("reagentInvalidDate")), cursor.getString(cursor.getColumnIndex("cabinetId")),
                        cursor.getString(cursor.getColumnIndex("drawerId")), cursor.getString(cursor.getColumnIndex("reagentPosition")),
                        cursor.getInt(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("reagentUser")));
                arrListReagents.add(reagent);
                cursor.moveToNext();
            }
        }
        return arrListReagents;
    }

    public boolean isReagentExist(String strReagentId) {
        Cursor cursor = db.query("reagent", null, "reagentId=? ", new String[] { strReagentId}, null, null, null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }           //wzj  add

    public Reagent getReagentById(String strReagentId) {
        Cursor cursor = db.rawQuery("select * from reagent where reagentId = '" + strReagentId + "'",null);
        Reagent reagent = null;
        ArrayList<Reagent> arrListReagents = new ArrayList<>();
        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                reagent = new Reagent(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getString(cursor.getColumnIndex("reagentName")), cursor.getString(cursor.getColumnIndex("reagentAlias")),
                        cursor.getString(cursor.getColumnIndex("reagentFormalName")), cursor.getString(cursor.getColumnIndex("reagentChemName")), cursor.getInt(cursor.getColumnIndex("reagentType")),
                        cursor.getString(cursor.getColumnIndex("reagentPurity")), cursor.getString(cursor.getColumnIndex("reagentSize")), cursor.getString(cursor.getColumnIndex("reagentTotalSize")),
                        cursor.getString(cursor.getColumnIndex("reagentCreater")), cursor.getString(cursor.getColumnIndex("reagentGoodsID")), cursor.getInt(cursor.getColumnIndex("reagentUnit")),
                        cursor.getString(cursor.getColumnIndex("reagentDensity")), cursor.getString(cursor.getColumnIndex("reagentInvalidDate")), cursor.getString(cursor.getColumnIndex("cabinetId")),
                        cursor.getString(cursor.getColumnIndex("drawerId")), cursor.getString(cursor.getColumnIndex("reagentPosition")),
                        cursor.getInt(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("reagentUser")));
                return reagent;
            }
        }
        return reagent;
    }

    public Reagent getReagentByPos(String strDrawerId,String strReagentPos) {
        Cursor cursor =  db.query("reagent", null, "drawerId=? and reagentPosition=?", new String[] { strDrawerId,strReagentPos }, null, null, null);
        Reagent reagent = null;
        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                reagent = new Reagent(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getString(cursor.getColumnIndex("reagentName")), cursor.getString(cursor.getColumnIndex("reagentAlias")),
                        cursor.getString(cursor.getColumnIndex("reagentFormalName")), cursor.getString(cursor.getColumnIndex("reagentChemName")), cursor.getInt(cursor.getColumnIndex("reagentType")),
                        cursor.getString(cursor.getColumnIndex("reagentPurity")), cursor.getString(cursor.getColumnIndex("reagentSize")), cursor.getString(cursor.getColumnIndex("reagentTotalSize")),
                        cursor.getString(cursor.getColumnIndex("reagentCreater")), cursor.getString(cursor.getColumnIndex("reagentGoodsID")), cursor.getInt(cursor.getColumnIndex("reagentUnit")),
                        cursor.getString(cursor.getColumnIndex("reagentDensity")), cursor.getString(cursor.getColumnIndex("reagentInvalidDate")), cursor.getString(cursor.getColumnIndex("cabinetId")),
                        cursor.getString(cursor.getColumnIndex("drawerId")), cursor.getString(cursor.getColumnIndex("reagentPosition")),
                        cursor.getInt(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("reagentUser")));
                return reagent;
            }
        }
        return reagent;
    }                                   //wzj  add


    public void deleteReagentById(String strReagentId){
        db.delete("reagent", "reagentId == ?", new String[]{ strReagentId });
    }

    public void deleteReagentByPos(String strDrawerId,String strReagentPos)
    {
        db.delete("reagent", "drawerId == ? and reagentPosition ==?", new String[]{ strDrawerId,strReagentPos });
    }

    public void addReagent(String strReagentId, String strReagentName, String strReagentAlias,
                           String strReagentFormalName, String strReagentChemName, int iReagentType,
                           String strReagentPurity, String strReagentSize, String strReagentTotalSize,
                           String strReagentCreater, String strReagentGoodsID, int iReagentUnit,
                           String strReagentDensity, String strReagentInvalidDate, String strCabinetId,
                           String strDrawerId, String strReagentPos, int iStatus, String strReagentUser) {
        Reagent reagent = new Reagent(strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize, strReagentTotalSize,
                strReagentCreater, strReagentGoodsID, iReagentUnit,
                strReagentDensity, strReagentInvalidDate, strCabinetId,
                strDrawerId, strReagentPos, iStatus, strReagentUser);
        db.execSQL("INSERT INTO reagent VALUES(null, ?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, ?)", new Object[]{strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize, strReagentTotalSize,
                strReagentCreater, strReagentGoodsID, iReagentUnit,
                strReagentDensity, strReagentInvalidDate, strCabinetId,
                strDrawerId, strReagentPos, iStatus, strReagentUser});
    }

    public void backReagent(String strReagentId, String strSize){
        ContentValues data=new ContentValues();
        data.put("status",SC_Const.EXIST);
        data.put("reagentSize", strSize);
        data.put("reagentUser", "");
        db.update("reagent", data, "reagentId=?", new String[]{strReagentId + ""});
    }

    public void updateReagentStatus(String strReagentId, int iStatus, String strUserId){
        Log.e(strReagentId, iStatus + "");
        ContentValues data=new ContentValues();
        data.put("status",iStatus);
        data.put("reagentUser",strUserId);
        db.update("reagent", data, "reagentId=?", new String[]{strReagentId + ""});
    }
    public void updateReagentSize(String strReagentId, String strReagentSize,String strReagentTotalSize){
        ContentValues data=new ContentValues();
        data.put("reagentSize",strReagentSize);
        data.put("reagentTotalSize",strReagentTotalSize);
        db.update("reagent", data, "reagentId=?", new String[]{strReagentId + ""});
    }
    public void updateReagentStatusByPos(String strDrawerId,String strReagentPos,String strReagentUser, int iStatus){
        ContentValues data=new ContentValues();
        data.put("status",iStatus);
        data.put("reagentUser",strReagentUser);
        db.update("reagent", data, "drawerId == ? and reagentPosition ==?", new String[]{strDrawerId,strReagentPos});
    }

    public void addReagentTemplate(String strReagentId, String strReagentName, String strReagentAlias,
                                   String strReagentFormalName, String strReagentChemName, int iReagentType,
                                   String strReagentPurity, String strReagentSize,
                                   String strReagentCreater, String strReagentGoodsID, String strReagentUnit,
                                   String strReagentDensity) {
        ReagentTemplate reagentTemplate = new ReagentTemplate(strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize,
                strReagentCreater, strReagentGoodsID, strReagentUnit,
                strReagentDensity);
        db.execSQL("INSERT INTO reagentTemplate VALUES(null, ?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?)", new Object[]{strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize, strReagentCreater,
                strReagentGoodsID, strReagentUnit, strReagentDensity});
    }
    public void deletReagentTemplateByInfo(String strReagentId, String strReagentName, String strReagentAlias,
                                           String strReagentFormalName, String strReagentChemName,
                                           String strReagentPurity, String strReagentSize,
                                           String strReagentCreater, String strReagentGoodsID, String strReagentUnit,
                                           String strReagentDensity)
        {
            db.delete("reagentTemplate","reagentId ==? and reagentName == ? and reagentAlias == ? and reagentFormalName == ? " +
                    "and reagentChemName == ?  and reagentPurity == ? and reagentSize == ? and  reagentCreater == ?" +
                    "and reagentGoodsID == ? and reagentUnit == ? and reagentDensity ==?",new String[]{strReagentId, strReagentName, strReagentAlias,
                    strReagentFormalName, strReagentChemName,
                    strReagentPurity, strReagentSize,
                    strReagentCreater, strReagentGoodsID, strReagentUnit,
                    strReagentDensity});

    }
    public ArrayList<ReagentTemplate> getReagentTemplate(){
        Cursor cursor = db.rawQuery("select * from reagentTemplate order by reagentName",null);
        ArrayList<ReagentTemplate> arrListReagentTempate = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ReagentTemplate reagentTemplate = new ReagentTemplate(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getString(cursor.getColumnIndex("reagentName")), cursor.getString(cursor.getColumnIndex("reagentAlias")),
                        cursor.getString(cursor.getColumnIndex("reagentFormalName")), cursor.getString(cursor.getColumnIndex("reagentChemName")), cursor.getInt(cursor.getColumnIndex("reagentType")),
                        cursor.getString(cursor.getColumnIndex("reagentPurity")), cursor.getString(cursor.getColumnIndex("reagentSize")), cursor.getString(cursor.getColumnIndex("reagentCreater")),
                        cursor.getString(cursor.getColumnIndex("reagentGoodsID")), cursor.getString(cursor.getColumnIndex("reagentUnit")), cursor.getString(cursor.getColumnIndex("reagentDensity")));
                arrListReagentTempate.add(reagentTemplate);
                cursor.moveToNext();
            }
        }
        return arrListReagentTempate;
    }


    //获取试剂柜编号
    public ArrayList<CabinetInfo> getCabinetNo() {
        Cursor cursor = db.rawQuery("select * from cabinet_no order by cabinet_no",null);
        ArrayList<CabinetInfo> arrListCabinetInfo = new ArrayList<>();
        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                CabinetInfo cabinetInfo = new CabinetInfo(cursor.getString(cursor.getColumnIndex("cabinet_no")), cursor.getString(cursor.getColumnIndex("cabinet_serviceCode")));
                arrListCabinetInfo.add(cabinetInfo);
                cursor.moveToNext();
            }
        }
        return arrListCabinetInfo;
    }

    public void deleteAllCabinetNo()
    {
        db.execSQL("DELETE FROM cabinet_no");
    }
    public void addCabinetNo(String cabinentNo,String serviceCode) {
        db.execSQL("INSERT INTO cabinet_no VALUES(null,?,?)", new Object[]{cabinentNo,serviceCode});
    }

    public void deleteAllReagentTemplate()
    {
        db.execSQL("DELETE FROM reagentTemplate");
    }

    public void closeDB() {
        db.close();
    }


    //----------------------------------drawer manage begin----------------------------//
    public ArrayList<ReagentUserRecord> getReagentUseRecord(){
        Cursor cursor = db.rawQuery("select * from reagentUserRecord",null);
        ArrayList<ReagentUserRecord> arrListReagentUserRecords= new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                arrListReagentUserRecords.add(new ReagentUserRecord(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getInt(cursor.getColumnIndex("operationType")),cursor.getString(cursor.getColumnIndex("operationTime")),cursor.getString(cursor.getColumnIndex("operator")),cursor.getString(cursor.getColumnIndex("reagentTotalSize")),cursor.getString(cursor.getColumnIndex("reagentSize")),cursor.getString(cursor.getColumnIndex("consumption"))));
                cursor.moveToNext();
            }
        }
        return arrListReagentUserRecords;
    }
    public void addReagentUserRecord(String reagentId, int operationType, String operationTime,String operator,String reagentTotalSize,String reagentSize,String consumption){
        db.execSQL("INSERT INTO reagentUserRecord VALUES(null, ?, ?, ?, ?, ?, ?, ?)", new Object[]{reagentId, operationType, operationTime,operator,reagentTotalSize,reagentSize,consumption});
    }
public ReagentUserRecord getReagentUseRecordByDate(String strdate)
{
    Cursor cursor =  db.query("reagentUserRecord", null, "operationTime=?", new String[] { strdate }, null, null, null);
    ReagentUserRecord reagentUserRecord = null;
    if (cursor.moveToFirst()) {
        if (!cursor.isAfterLast()) {
            reagentUserRecord = new ReagentUserRecord(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getInt(cursor.getColumnIndex("operationType")), cursor.getString(cursor.getColumnIndex("operationTime")),
                    cursor.getString(cursor.getColumnIndex("operator")), cursor.getString(cursor.getColumnIndex("reagentTotalSize")), cursor.getString(cursor.getColumnIndex("reagentSize")),
                    cursor.getString(cursor.getColumnIndex("consumption")));
            return reagentUserRecord;
        }
    }
    return reagentUserRecord;
}

    //----------------------------------drawer manage end----------------------------//
    public void addSrapReagent(String strReagentId, String strReagentName, String strReagentAlias,
                           String strReagentFormalName, String strReagentChemName, int iReagentType,
                           String strReagentPurity, String strReagentSize, String strReagentTotalSize,
                           String strReagentCreater, String strReagentGoodsID, int iReagentUnit,
                           String strReagentDensity, String strReagentInvalidDate, String strCabinetId,
                           String strDrawerId, String strReagentPos, int iStatus, String strReagentUser) {
        Reagent reagent = new Reagent(strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize, strReagentTotalSize,
                strReagentCreater, strReagentGoodsID, iReagentUnit,
                strReagentDensity, strReagentInvalidDate, strCabinetId,
                strDrawerId, strReagentPos, iStatus, strReagentUser);
        db.execSQL("INSERT INTO scrapReagent VALUES(null, ?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, ?)", new Object[]{strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize, strReagentTotalSize,
                strReagentCreater, strReagentGoodsID, iReagentUnit,
                strReagentDensity, strReagentInvalidDate, strCabinetId,
                strDrawerId, strReagentPos, iStatus, strReagentUser});
    }
    public boolean isScrapReagentExist(String strReagentId) {
        Cursor cursor = db.query("scrapReagent", null, "reagentId=? ", new String[] { strReagentId}, null, null, null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }           //wzj  add



    //----------------------------------sysSeting manage ----------------------------//
    public ArrayList<SysSeting> getSysSeting() {
        Cursor cursor = db.rawQuery("select * from sysSeting",null);
        ArrayList<SysSeting> arrListSysSeting = new ArrayList<>();
        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
               SysSeting sysSeting = new SysSeting(cursor.getString(cursor.getColumnIndex("serialNum")), cursor.getString(cursor.getColumnIndex("cameraVersion")));
                arrListSysSeting.add(sysSeting);
                cursor.moveToNext();
            }
        }
        return arrListSysSeting;
    }

    public void deleteAllSysSeting()
    {
        db.execSQL("DELETE FROM sysSeting");
    }
    public void addSysSeting(String serialNum,String cameraVersion) {
        db.execSQL("INSERT INTO sysSeting VALUES(null,?,?)", new Object[]{serialNum,cameraVersion});
    }
}
