package com.example.newui_smartdrawer.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class OperationRecord {
    public String cabinetNo;
    public String operationType;
    public String operationUser;
    public String operationTime;
    public String reagentName;

    public String getCabinetNo() {
        return cabinetNo;
    }

    public void setCabinetNo(String cabinetNo) {
        this.cabinetNo = cabinetNo;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getReagentName() {
        return reagentName;
    }

    public void setReagentName(String operationName) {
        this.reagentName = operationName;
    }

    public String toJsonString() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("cabinetNo", this.cabinetNo);
        jo.put("operationType", this.operationType);
        jo.put("operationUser", this.operationUser);
        jo.put("operationTime", this.operationTime);
        jo.put("reagentName", this.reagentName);
        return jo.toString();
    }

}
