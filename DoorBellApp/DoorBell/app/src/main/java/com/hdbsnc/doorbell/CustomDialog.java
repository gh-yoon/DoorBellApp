package com.hdbsnc.doorbell;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HDBSNC on 2018-03-05.
 */

public class CustomDialog extends Dialog implements View.OnClickListener{
    private static final int LAYOUT = R.layout.dialog_custom;
    private Context context;
    private TextInputEditText inputID;
    private TextInputEditText inputIP;
    private TextInputEditText inputPW;
    private TextView cancelTv;
    private TextView registerTv;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private String name;
    public CustomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        pref = context.getSharedPreferences("deviceID", 0);
        editor = pref.edit();
    }

    public CustomDialog(Context context,String name){
        super(context);
        this.context = context;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        inputID = (TextInputEditText) findViewById(R.id.inputID);
        inputIP = (TextInputEditText) findViewById(R.id.inputIP);
        inputPW = (TextInputEditText) findViewById(R.id.inputPW);

        cancelTv = (TextView) findViewById(R.id.cancel);
        registerTv = (TextView) findViewById(R.id.register);

        cancelTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);

        if(name != null){
            inputID.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                cancel();
                break;
            case R.id.register:
                //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection conn = null;
                        //Context context = CustomDialog.super.getContext();
                        try {
                            URL url = new URL("http://192.168.76.49:8080/mvc/mobile/add"); //요청 URL을 입력
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)
                            conn.setDoInput(true); //input을 사용하도록 설정 (default : true)
                            conn.setDoOutput(true); //output을 사용하도록 설정 (default : false)
                            conn.setConnectTimeout(500); //타임아웃 시간 설정 (default : 무한대기)
                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8")); //캐릭터셋 설정
                            writer.write(
                                    "id=" + inputID.getText() + "&ip=" + inputIP.getText() + "&pw=" + inputPW.getText()
                            ); //요청 파라미터를 입력
                            writer.flush();
                            writer.close();
                            os.close();
                            conn.connect();

                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //캐릭터셋 설정

                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                if (sb.length() > 0) {
                                    sb.append("\n");
                                }
                                sb.append(line);
                            }
                            String sbtext = sb.toString();
                            if (sbtext.equals("ok")) {
                                editor.putString("deviceID", String.valueOf(inputID.getText()));
                                editor.commit();
                                System.out.println("response:" + sb.toString() + "Device입력 성공");
                            } else {
                                System.out.println("response:" + sb.toString() + "Device입력 실패");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (conn != null) {
                                conn.disconnect();
                            }
                        }
                    }
                }).start();
                break;
        }
    }
}
