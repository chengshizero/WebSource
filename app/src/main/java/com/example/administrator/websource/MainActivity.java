package com.example.administrator.websource;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText et_path;
    private TextView tv_result;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REQUESTSUCESS:
                    String conntent = (String) msg.obj;
                    tv_result.setText(conntent);
                    break;
                case REQUESTNOTFOUND:
                        Toast.makeText(getApplicationContext(),"不存在",Toast.LENGTH_SHORT).show();
                    break;
                case REQUESTEXCEPTION:
                        Toast.makeText(getApplicationContext(),"網路or網址錯誤",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private  final int REQUESTSUCESS =0;
    private  final int REQUESTNOTFOUND =1;
    private  final int REQUESTEXCEPTION =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        et_path = (EditText)findViewById(R.id.et_path);
        tv_result = (TextView)findViewById(R.id.tv_result);
    }
    public void click(View v){
        new Thread(){
            @Override
            public void run() {
                try {
                    String path = et_path.getText().toString().trim();
                    URL url = new URL(path);
                    HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    int code = conn.getResponseCode();
                    if(code==200){
                        InputStream in = conn.getInputStream();
                        String conntent = StreamTools.readStream(in);

                        Message msg = Message.obtain();
                        msg.what = REQUESTSUCESS;
                        msg.obj = conntent;
                        handler.sendMessage(msg);

                    }else if(code==404){
                        Message msg = Message.obtain();
                        msg.what = REQUESTNOTFOUND;
                        handler.sendMessage(msg);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = REQUESTEXCEPTION;
                    handler.sendMessage(msg);
                }

            }
        }.start();



    }
}
