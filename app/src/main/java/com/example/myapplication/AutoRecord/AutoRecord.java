package com.example.myapplication.AutoRecord;

import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.myapplication.AutoRecord.HttpUtil;
import com.example.myapplication.AutoRecord.InfoBean.ResponseStuInfoBean;
import com.example.myapplication.AutoRecord.LoginBean.LoginResponseBean;
import com.google.gson.Gson;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.Base64;

public class AutoRecord {
    private static  String TAG ="AUTO_RECORD";
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void autoRecord(final String user, final String psw, final HttpUtil.CallBack callBack) {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                super.run();
                record(user, psw, callBack);
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void record(String user, String psw, final HttpUtil.CallBack callBack) {
        try {
            final Gson gson = HttpUtil.gson;
            //密钥
            String sessionKey = "xAt9Ye&SouxCJziN";
            //向量
            String iv = "xAt9Ye&SouxCJziN";

            //用Base64编码
            Base64.Encoder encoder = Base64.getEncoder();
            String baseData = encoder.encodeToString(psw.getBytes());
            String baseSessionKey = encoder.encodeToString(sessionKey.getBytes());
            String baseIv = encoder.encodeToString(iv.getBytes());

            //导入支持AES/CBC/PKCS7Padding的Provider
            Security.addProvider(new BouncyCastleProvider());

            //获取加密数据
            String encryptedData = AESUtil.encrypt(baseData, baseSessionKey, baseIv);

            //登录尝试
            HttpUtil httpUtil = new HttpUtil();
            final LoginResponseBean loginResponseBean = new LoginResponseBean();//用于存储登录后的信息
            final ResponseStuInfoBean responseStuInfoBean = new ResponseStuInfoBean();//用于获取登录后的学生信息
            httpUtil.login(user, encryptedData, new HttpUtil.CallBack() {
                @Override
                public void onSuccess(Object data) {
                    LoginResponseBean lr = gson.fromJson((String) data, LoginResponseBean.class);
                    loginResponseBean.setMeta(lr.getMeta());
                    loginResponseBean.setData(lr.getData());
                }

                @Override
                public void onFail(String errorInfo, String code) {
                    callBack.onFail(errorInfo, code);
                }
            });
            if (loginResponseBean.getMeta() != null && loginResponseBean.getMeta().isSuccess()){
                httpUtil.getStuInfo(loginResponseBean.getData().getJnuid(), "1", new HttpUtil.CallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        ResponseStuInfoBean rsib = gson.fromJson((String) data, ResponseStuInfoBean.class);
                        Log.d(TAG, "onSuccess: "+"已经存储过的信息：" + gson.toJson(rsib));
                        //meta的response：1070（未打卡），1071（打卡）
                        responseStuInfoBean.setMeta(rsib.getMeta());
                        responseStuInfoBean.setData(rsib.getData());
                    }

                    @Override
                    public void onFail(String errorInfo, String code) {
                        callBack.onFail(errorInfo, code);
                    }
                });//idype是1
            }else{
                callBack.onFail("login fail!","0");
                return;
            }

            if (responseStuInfoBean.getMeta() != null && responseStuInfoBean.getMeta().isSuccess()){
                httpUtil.updateStuInfoAtSchool(responseStuInfoBean, loginResponseBean.getData().getJnuid(), new HttpUtil.CallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        callBack.onSuccess(data);
                    }

                    @Override
                    public void onFail(String errorInfo, String code) {
                        callBack.onFail(errorInfo, code);
                    }
                });
            }else {
                callBack.onFail("fail to auto record!student data maybe wrong!","0");
                return;
            }
        } catch (Exception e) {
            callBack.onFail("something wrong!", "0");
        }
    }
}
