package com.example.newui_smartdrawer.util;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by james on 19/01/2018.
 */

public class OperationRecordService {
    public OperationRecordService() {
    }

    public void uploadOneRecord(String cabinetNo, String operationType, String user, String reagentName){

        OperationRecord or = new OperationRecord();
        or.setCabinetNo(cabinetNo);
        or.setOperationType(operationType);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        or.setOperationTime(format.format(new Date()));
        or.setOperationUser(user);
        or.setReagentName(reagentName);
        try {
            doPostOneRecord(or);
        } catch (JSONException e) {
            Log.e(operationType,"记录上传失败");
        }
    }

    public void doPostOneRecord(OperationRecord or) throws JSONException {
        OkHttpClient mOkHttpClient=new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, or.toJsonString());
        Request request = new Request.Builder()
                .url("https://www.anchu365.com/admin/api/operation-record/insertOneRecord")
                .post(body)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("requestResponse", "failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("requestResponse", str);

            }

        });
    }
}
