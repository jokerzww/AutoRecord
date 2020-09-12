package com.example.myapplication.AutoRecord;

import android.util.Log;

import com.example.myapplication.AutoRecord.InfoBean.ResponseStuInfoBean;
import com.example.myapplication.AutoRecord.LoginBean.JnuIdInfo;
import com.example.myapplication.AutoRecord.LoginBean.LoginInfo;
import com.example.myapplication.AutoRecord.UploadInfoBean.UploadInfoBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpUtil {
    private static final String TAG = "HTTP_UTIl";

    public interface CallBack{
        void onSuccess(Object data);
        void onFail(String errorInfo,String code);
    }
    private static OkHttpClient okHttpClient;
    public static Gson gson=new GsonBuilder().disableHtmlEscaping().create();
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public HttpUtil(){
        if(okHttpClient==null)
            okHttpClient=new OkHttpClient();
        if(gson==null)
            gson=new GsonBuilder().disableHtmlEscaping().create();
    }

    public void postJsonToUrl(String data,String url,CallBack callBack) {
        RequestBody body = RequestBody.create(data,JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            callBack.onSuccess(response.body().string());
        }catch (IOException e){
            callBack.onFail("Net error","0");
        }
    }

    public void postJsonToUrlEnqueue(String data, String url, final CallBack callBack){
        RequestBody body = RequestBody.create(data,JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
//1.异步请求，通过接口回调告知用户 http 的异步执行结果
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
                callBack.onFail(e.getMessage(),null);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body().string());
                }
            }
        });
    }



    public void login(String userName,String pswEncrypted,CallBack callBack) {
        LoginInfo loginInfo=new LoginInfo();
        loginInfo.setUsername(userName);
        loginInfo.setPassword(pswEncrypted);
        String url=new String("https://stuhealth.jnu.edu.cn/api/user/login");
        Log.d(TAG, "login: "+"登录信息"+gson.toJson(loginInfo));
        postJsonToUrl(gson.toJson(loginInfo),url,callBack);
    }

    public void getStuInfo(String jnuId,String idType,CallBack callBack) throws IOException{
        JnuIdInfo jnuIdInfo=new JnuIdInfo();
        jnuIdInfo.setJnuid(jnuId);
        jnuIdInfo.setIdType(idType);
        String url=new String("https://stuhealth.jnu.edu.cn/api/user/stuinfo");
        Log.d(TAG, "getStuInfo: "+"jnuid信息："+gson.toJson(jnuIdInfo));
        postJsonToUrl(gson.toJson(jnuIdInfo),url,callBack);
    }

    public void updateStuInfoAtSchool(ResponseStuInfoBean responseStuInfoBean, String jnuID, CallBack callBack){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        UploadInfoBean uploadInfoBean=new UploadInfoBean();
        uploadInfoBean.setJnuid(jnuID);
        String url=new String("https://stuhealth.jnu.edu.cn/api/write/main");
         uploadInfoBean.setMainTable(responseStuInfoBean.getData().getMainTable().getSerializableCopy());
         uploadInfoBean.getMainTable().setDeclareTime(df.format(new Date()));
         uploadInfoBean.getMainTable().setPersonName(responseStuInfoBean.getData().getXm());
         uploadInfoBean.getMainTable().setSex(responseStuInfoBean.getData().getXbm());
         uploadInfoBean.getMainTable().setProfessionName(responseStuInfoBean.getData().getZy());
         uploadInfoBean.getMainTable().setCollegeName(responseStuInfoBean.getData().getYxsmc());
        Log.d(TAG, "updateStuInfoAtSchool: "+"上传信息："+gson.toJson(uploadInfoBean));
        postJsonToUrl(gson.toJson(uploadInfoBean),url,callBack);
    }

}
