package com.example.newui_smartdrawer.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadRecordManager {
    //记录上传得服务器接口
    private Context context;
    JSONObject json1=new JSONObject();
    public UploadRecordManager(Context context) {
        this.context = context;
    }

    public void getCode(String cabinetNo,String operationnType,String operationUser,String operationTime,String reagentName,String reagentId,
    String totalWeight,String surplus,String consumption, String position) {

        try {
            json1.put("cabinetNo", cabinetNo);
            json1.put("operationType", operationnType);
            json1.put("operationUser", operationUser);
            json1.put("operationTime", operationTime);
            json1.put("reagentName", reagentName);
            json1.put("reagentId",reagentId);
            json1.put("totalWeight",totalWeight);
            json1.put("surplus",surplus);
            json1.put("consumption",consumption);
            json1.put("position",position);
            //构建一个json数据

//            Log.d("wzj","json数据为:"+json1.toString());
        }catch(JSONException e) {
            Log.d("wzj", "json数据为:" + json1.toString());

            //TODOAuto-generated catch block

            e.printStackTrace();

        }
            new Thread() {
                public void run() {

                    try {
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, json1.toString());
                        Request request = new Request.Builder()
                                .url("https://www.anchu365.com/admin/api/operation-record/insertOneRecord")
                                .post(body)
                                .build();
                        okhttp3.Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {
                                Log.e("requestResponse", "failure");
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                                String str = response.body().string();
                                Log.e("requestResponse", str);

                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }.start();

        }

}
