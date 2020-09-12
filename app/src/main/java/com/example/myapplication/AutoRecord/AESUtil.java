package com.example.myapplication.AutoRecord;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESUtil {
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String data, String sessionKey, String iv) throws Exception {
        //加密之前，先从Base64格式还原到原始格式
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] dataByte = decoder.decode(data);
        byte[] keyByte = decoder.decode(sessionKey);
        byte[] ivByte = decoder.decode(iv);

        String encryptedData = null;

        //指定算法，模式，填充方式，创建一个Cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");

        //生成Key对象
        Key sKeySpec = new SecretKeySpec(keyByte, "AES");

        //把向量初始化到算法参数
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(ivByte));

        //指定用途，密钥，参数 初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, params);

        //指定加密
        byte[] result = cipher.doFinal(dataByte);

        //对结果进行Base64编码，否则会得到一串乱码，不便于后续操作
        Base64.Encoder encoder = Base64.getEncoder();
        encryptedData = encoder.encodeToString(result);


        return encryptedData;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String encryptedData, String sessionKey, String iv) throws Exception {

        //解密之前先把Base64格式的数据转成原始格式
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] dataByte = decoder.decode(encryptedData);
        byte[] keyByte = decoder.decode(sessionKey);
        byte[] ivByte = decoder.decode(iv);

        String data = null;

        //指定算法，模式，填充方法 创建一个Cipher实例
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");

        //生成Key对象
        Key sKeySpec = new SecretKeySpec(keyByte, "AES");

        //把向量初始化到算法参数
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(ivByte));

        //指定用途，密钥，参数 初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);

        //执行解密
        byte[] result = cipher.doFinal(dataByte);

        //解密后转成字符串
        data = new String(result);

        return data;
    }
}