package com.hdbsnc.doorbell;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
SharedPreferences pref;
SharedPreferences dpref;
SharedPreferences.Editor editor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openOptionsDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openOptionsDialog() {
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("ALL");

        final WebView mywebview = (WebView) findViewById(R.id.webView1);
        mywebview.setWebViewClient(new WebViewClient());
        mywebview.loadUrl("http://192.168.76.60:8080/stream");


        pref = getSharedPreferences("checked", 0);
        dpref = getSharedPreferences("deviceID", 0);
        editor = pref.edit();
        Switch cSwitch = (Switch)findViewById(R.id.switch1);

        if(pref.getBoolean("checked", false)){
            cSwitch.setChecked(true);
        }else{
            cSwitch.setChecked(false);
        }


        final Button picbtn = (Button) findViewById(R.id.picbtn);
        final Handler mHandler = new Handler();
        picbtn.setOnClickListener(new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(new Runnable() {
                @Override public void run() {
                    // TODO Auto-generated method stub
                    try{
                        Socket soc=new Socket("192.168.76.60",2004);
                        DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
                        dout.writeUTF("send");
                        dout.flush();
                        dout.close();
                        soc.close();

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run () {
                                // 실행할 동작 코딩
                                mywebview.loadUrl("http://192.168.76.60:8080/stream");
                                mHandler.sendEmptyMessage(0);
                                // 실행이 끝난후 알림
                                }
                            },2000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }}
            }).start();}});

    cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // do something, the isChecked will be
            // true if the switch is in the On position
            if(isChecked){
                editor.putBoolean("checked", true);
                editor.commit();
                Log.i(dpref.getString("deviceID", "none"),"수신 로그__________________________________!!!!!!!!!!!!!!!!!!!!!!!!");
                FirebaseMessaging.getInstance().subscribeToTopic(dpref.getString("deviceID", "none"));
            }
            else{
                editor.putBoolean("checked", false);
                editor.commit();
                Log.i(dpref.getString("deviceID", "none"),"거절 로그__________________________________!!!!!!!!!!!!!!!!!!!!!!!!");
                FirebaseMessaging.getInstance().unsubscribeFromTopic(dpref.getString("deviceID", "none"));
            }
        }
    });


    Button btn2 = (Button) findViewById(R.id.button2);
    btn2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    int valueMode =  ((AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE)).getRingerMode();
    Toast.makeText(getApplicationContext(), "현재 모드: " + valueMode, Toast.LENGTH_SHORT);
    }
}

